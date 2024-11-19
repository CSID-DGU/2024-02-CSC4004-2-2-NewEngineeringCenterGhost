import torch
from torch.amp import autocast, GradScaler
from torch.utils.data import DataLoader
import time
from module.custom_model import Model
from module.utils import *
from module.dataset import split_dataset
import gc

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

epochs = 15
batch_size = 8
update_size = 64 // batch_size

train_dataset, val_dataset, test_dataset = split_dataset()
train_loader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True)
val_loader = DataLoader(val_dataset, batch_size=batch_size, shuffle=False)
test_loader = DataLoader(test_dataset, batch_size=batch_size, shuffle=False)

model = Model().to(device)
criterion = torch.nn.BCEWithLogitsLoss()
metrics = Metrics()
optimizer = torch.optim.AdamW(model.parameters(), lr=1e-4)
scaler = GradScaler()

def run_epoch(loader: DataLoader, train=False) -> tuple: # return loss, f1, acc
    if train:
        model.train()
    else:
        model.eval()
    
    total_loss = 0
    total_batch = len(loader)
    metrics.reset()

    t_start = time.time()
    
    torch.cuda.empty_cache()
    torch.cuda.synchronize()
    for i, (x, padding_mask, y) in enumerate(loader):
        x = x.to(device)
        padding_mask = padding_mask.to(device)
        y = y.to(device).unsqueeze(-1)
        with autocast('cuda', torch.bfloat16):
            pred, _ = model(x, padding_mask)
            loss = criterion(pred, y)

        if train:
            scaler.scale(loss).backward()

        total_loss += loss.item()
        metrics.update(pred > 0, y)
        
        f1 = metrics.get('f1')
        acc = metrics.get('acc')
        eta, elapsed = getETA(t_start, time.time(), i + 1, total_batch)
        clearPrint()
        printBar(i + 1, total_batch, 'Training: ' if train else 'Validation: ',
                 f'{elapsed:8.1f}s/{eta:8.1f}s | loss: {total_loss / (i + 1):.4f} | F1: {f1:.4f} | Acc: {acc:.4f}')
        
        if not train:
            continue

        if (i + 1) % update_size == 0 or i == total_batch - 1:
            scaler.step(optimizer)
            scaler.update()
            optimizer.zero_grad()
    print()
    return total_loss / total_batch, metrics.get('f1'), metrics.get('acc')

best_f1 = -1
history = History(['loss', 'f1', 'acc'])
for epoch in range(epochs):
    print(f'Epoch {epoch + 1}/{epochs}')
    history.update(run_epoch(train_loader, train=True), 'train')
    history.update(run_epoch(val_loader), 'val')
    history.save('train/history.png')

    result = f'Training: loss={history["train_loss"][-1]:.4f}, f1={history["train_f1"][-1]:.4f}, acc={history["train_acc"][-1]:.4f}'
    result += f' | Validation: loss={history["val_loss"][-1]:.4f}, f1={history["val_f1"][-1]:.4f}, acc={history["val_acc"][-1]:.4f}'

    clearPrint(2)
    print(f'{result}\n')

    if history['val_f1'][-1] > best_f1:
        best_f1 = history['val_f1'][-1]
        torch.save(model.state_dict(), 'train/model.pth')
        print('Model saved!\n')
    gc.collect()

print('Testing...')
model.load_state_dict(torch.load('train/model.pth', weights_only=True))
test_loss, test_f1, test_acc = run_epoch(test_loader)
clearPrint(2)
print('Best model test result:')
print(f'Test: loss={test_loss:.4f}, f1={test_f1:.4f}, acc={test_acc:.4f}')