import torch
from torch.amp import autocast, GradScaler
from torch.utils.data import DataLoader, ConcatDataset
from module.custom_model import Model
from module.utils import printBar, DatasetLoader, Metrics, split_dataset, History
import gc

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

epochs = 15
load_size = 4 # Number of datasets to load at once
batch_size = 8
update_size = 64 // batch_size

train_loader, val_loader, test_loader = split_dataset()

model = Model().to(device)
criterion = torch.nn.BCEWithLogitsLoss()
metrics = Metrics()
optimizer = torch.optim.AdamW(model.parameters(), lr=1e-4)
scaler = GradScaler()

def run_epoch(loader: DatasetLoader, train=False) -> tuple: # return loss, f1, acc
    if train:
        model.train()
    else:
        model.eval()
    
    total_loss = 0
    total_batch = 0
    datasets = []
    loader.shuffle()
    metrics.reset()
    for i in range(len(loader)):
        datasets.append(loader[i])
        if len(datasets) < load_size and i < len(loader) - 1:
            continue

        dataset = ConcatDataset(datasets)
        datasets = []
        dataloader = DataLoader(dataset, batch_size=batch_size, shuffle=True)

        loaded = []
        for j, (x, padding_mask, y) in enumerate(dataloader):
            loaded.append((x.to(device), padding_mask.to(device), y.to(device).unsqueeze(-1)))

            if len(loaded) < update_size and j < len(dataloader) - 1:
                continue
            
            torch.cuda.synchronize()
            for k, (x, padding_mask, y) in enumerate(loaded):
                total_batch += 1
                with autocast('cuda', torch.bfloat16):
                    pred, _ = model(x, padding_mask)
                    loss = criterion(pred, y) / update_size
                if train:
                    scaler.scale(loss).backward()
                total_loss += loss.item() * update_size
                metrics.update(pred > 0, y) 
            
                f1 = metrics.get('f1')
                acc = metrics.get('acc')
                printBar(i + 1, len(loader), '\rTotal Dataset:')
                printBar(k + j - len(loaded) + 2, len(dataloader), 'Training:' if train else 'Validation:',
                         f'{total_batch:6d} batches done | loss: {total_loss / total_batch:.4f} | F1: {f1:.4f} | Acc: {acc:.4f}')
            
            loaded = []
            torch.cuda.empty_cache()

            if not train:
                continue

            scaler.step(optimizer)
            scaler.update()
            optimizer.zero_grad()

        dataloader = None
        dataset = None
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
    print(f'{result}\n')

    if history['val_f1'][-1] > best_f1:
        best_f1 = history['val_f1'][-1]
        torch.save(model.state_dict(), 'train/model.pth')
        print('Model saved!\n')
    gc.collect()

print('Testing...')
model.load_state_dict(torch.load('train/model.pth', weights_only=True))
test_loss, test_f1, test_acc = run_epoch(test_loader)
print(f'Test: loss={test_loss:.4f}, f1={test_f1:.4f}, acc={test_acc:.4f}')