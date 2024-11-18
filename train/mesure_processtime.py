import torch
from torch.utils.data import DataLoader, ConcatDataset
from module.utils import split_dataset, printBar
from module.preprocessing import preprocess
import time
import pandas as pd

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

load_size = 4 # Number of datasets to load at once
batch_size = 8
update_size = 512 // batch_size

train_loader, val_loader, test_loader = split_dataset()

# mesure preprocess time
start = time.time()
data_path = "train/data/data.jsonl"
data = pd.read_json(data_path, lines=True)

for i, x in enumerate(data["sentence"]):
    x, padding_mask = preprocess(x, 3584)
    x = torch.tensor(x, dtype=torch.bfloat16).to(device)
    padding_mask = torch.tensor(padding_mask).to(device)
    printBar(i + 1, len(data))

print(f"\n1번 방법: {time.time() - start:.4f} sec")


start = time.time()
datasets = []
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

        printBar(i + 1, len(train_loader), '\rTotal Dataset:')
        printBar(j + 1, len(dataloader), 'Training:')
        if len(loaded) < update_size and j < len(dataloader) - 1:
            continue
        
        # skip training
        loaded = []
        torch.cuda.empty_cache()
    dataloader = None
    dataset = None

print(f"\n2번 방법: {time.time() - start:.4f} sec")
