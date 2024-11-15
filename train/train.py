import torch
from torch.amp import autocast, GradScaler
from torch.utils.data import DataLoader, ConcatDataset
from module.custom_model import Model
from module.utils import printBar, DatasetLoader, Metrics, split_dataset

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

epochs = 20
load_size = 4 # Number of datasets to load at once
batch_size = 8
update_size = 32 // batch_size

train_loader, val_loader, test_loader = split_dataset()

model = Model().to(device)
criterion = torch.nn.BCEWithLogitsLoss()
metrics = Metrics()
optimizer = torch.optim.AdamW(model.parameters(), lr=1e-4)
scaler = GradScaler()

best_f1 = 0
for epoch in range(epochs):
    print(f'Epoch {epoch + 1}/{epochs}')
    train_loss = 0
    total_batch = 0
    datasets = []
    train_loader.shuffle()
    metrics.reset()
    for i in range(len(train_loader)):
        datasets.append(train_loader[i])
        if len(datasets) < load_size and i < len(train_loader) - 1:
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
                    pred = model(x, padding_mask)
                    loss = criterion(pred, y) / update_size
                scaler.scale(loss).backward()
                train_loss += loss.item() * update_size
                metrics.update(pred > 0, y) 

                f1 = metrics.get('f1')
                acc = metrics.get('acc')
                printBar(i + 1, len(train_loader), '\rTotal Dataset:')
                printBar(k + j - len(loaded) + 2, len(dataloader), 'Training:', f'{total_batch:6d} batches done | loss: {train_loss / total_batch:.4f} | F1: {f1:.4f} | Acc: {acc:.4f}')

            loaded = []
            torch.cuda.empty_cache()

            scaler.step(optimizer)
            scaler.update()
            optimizer.zero_grad()
        dataloader = None
        dataset = None
    print()

    val_loss = 0
    total_batch = 0
    datasets = []
    val_loader.shuffle()
    metrics.reset()
    for i in range(len(val_loader)):
        datasets.append(val_loader[i])
        if len(datasets) < load_size and i < len(val_loader) - 1:
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
                    pred = model(x, padding_mask)
                    loss = criterion(pred, y) / update_size
                val_loss += loss.item() * update_size
                metrics.update(pred > 0, y) 

                f1 = metrics.get('f1')
                acc = metrics.get('acc')
                printBar(i + 1, len(val_loader), '\rTotal Dataset:')
                printBar(k + j - len(loaded) + 2, len(dataloader), 'Validation:', f'{total_batch:6d} batches done | loss: {val_loss / total_batch:.4f} | F1: {f1:.4f} | Acc: {acc:.4f}')

            loaded = []
            torch.cuda.empty_cache()

        dataloader = None
        dataset = None
    print()
    if f1 > best_f1:
        best_f1 = f1
        torch.save(model.state_dict(), 'train/model.pth')
        print(f'Saved model with F1: {best_f1:.4f}')