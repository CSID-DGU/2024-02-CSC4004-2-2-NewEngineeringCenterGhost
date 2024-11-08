import torch
from torch.amp import autocast, GradScaler
from torch.utils.data import DataLoader, ConcatDataset
from module.custom_model import Model
from module.utils import printBar, DatasetLoader, Metrics

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

epochs = 20
load_size = 6 # Number of datasets to load at once
batch_size = 12
update_size = 600 // batch_size

datasetLoader = DatasetLoader(max_len=3584)

model = Model().to(device)
criterion = torch.nn.BCEWithLogitsLoss()
metrics = Metrics()
optimizer = torch.optim.AdamW(model.parameters(), lr=1e-4)
scaler = GradScaler()

for epoch in range(epochs):
    print(f'Epoch {epoch + 1}/{epochs}')
    datasetLoader.shuffle()
    datasets = []
    train_loss = 0
    total_batch = 0
    for i in range(len(datasetLoader)):
        datasets.append(datasetLoader[i])
        if len(datasets) < load_size and i < len(datasetLoader) - 1:
            continue

        dataset = ConcatDataset(datasets)
        datasets = []
        dataloader = DataLoader(dataset, batch_size=batch_size, shuffle=True)

        torch.cuda.synchronize()
        for j, (x, padding_mask, y) in enumerate(dataloader):
            total_batch += 1

            x = torch.tensor(x, dtype=torch.bfloat16, device=device)
            padding_mask = torch.tensor(padding_mask, dtype=torch.bool, device=device)
            y = torch.tensor(y, dtype=torch.bfloat16, device=device).unsqueeze(1)
            torch.cuda.empty_cache()

            with autocast('cuda', torch.bfloat16):
                pred = model(x, padding_mask)
                loss = criterion(pred, y) / update_size
            scaler.scale(loss).backward()

            if (j + 1) % update_size == 0 or j == len(dataloader) - 1:
                scaler.step(optimizer)
                scaler.update()
                optimizer.zero_grad()

            train_loss += loss.item() * update_size
            printBar(i + 1, len(datasetLoader), '\rTotal Dataset:')
            printBar(j + 1, len(dataloader), 'Training:', f'{total_batch:6d} batches trained | loss: {train_loss / total_batch:.4f}')
        dataloader = None
        dataset = None
    print(f'Training loss: {train_loss / total_batch:.4f}')