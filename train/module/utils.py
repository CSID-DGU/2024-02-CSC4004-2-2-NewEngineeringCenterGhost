import os
from torch.utils.data import Dataset
from sklearn.model_selection import train_test_split
import numpy as np
import torch

class SeqDataset(Dataset):
    def __init__(self, x, y, max_len):
        self.x = x
        self.y: torch.Tensor = y.type(torch.bfloat16)
        self.max_len: int = max_len

    def __len__(self):
        return len(self.x)
    
    def __getitem__(self, idx):
        padding_mask = torch.zeros((self.max_len), dtype=torch.bool)
        _len = len(self.x[idx])
        padding_mask[_len:] = True
        return torch.cat((self.x[idx], torch.zeros((self.max_len - _len, self.x[idx].shape[1]), dtype=torch.bfloat16)), dim=0), padding_mask, self.y[idx]

class DatasetLoader():
    def __init__(self, max_len, X, y):
        self.max_len = max_len
        self.X = X
        self.y = y
        self.len = len(X)

    def shuffle(self):
        idx = np.random.permutation(self.len)
        self.X = [self.X[i] for i in idx]
        self.y = [self.y[i] for i in idx]

    def __len__(self):
        return self.len
    
    def __getitem__(self, idx):
        _X = torch.load(self.X[idx], weights_only=False)
        _y = torch.load(self.y[idx], weights_only=False)
        return SeqDataset(_X, _y, self.max_len)

def split_dataset(train_size=0.9, max_len=3584):
    dataset_dir = "train/data/dataset"
    prefix = "dataset_"
    _len = len(os.listdir(dataset_dir)) // 2
    X = []
    y = []
    for i in range(_len):
        X.append(f'{dataset_dir}/{prefix}{i:02d}_X.pt')
        y.append(f'{dataset_dir}/{prefix}{i:02d}_y.pt')
    
    X_train, X_val, y_train, y_val = train_test_split(X, y, train_size=train_size)
    X_val, X_test, y_val, y_test = train_test_split(X_val, y_val, train_size=0.5)
    return DatasetLoader(max_len, X_train, y_train), DatasetLoader(max_len, X_val, y_val), DatasetLoader(max_len, X_test, y_test)


class Metrics():
    def __init__(self):
        self.reset()
    
    def reset(self):
        self.tp = 0
        self.fp = 0
        self.tn = 0
        self.fn = 0
        self.prec = []
        self.recall = []
        self.f1 = []
        self.acc = []

    def update(self, pred, target):
        self.tp = ((pred == 1) & (target == 1)).sum().item()
        self.fp = ((pred == 1) & (target == 0)).sum().item()
        self.tn = ((pred == 0) & (target == 0)).sum().item()
        self.fn = ((pred == 0) & (target == 1)).sum().item()

        self.prec.append(self.tp / (self.tp + self.fp + 1e-8))
        self.recall.append(self.tp / (self.tp + self.fn + 1e-8))
        self.f1.append(2 * self.prec[-1] * self.recall[-1] / (self.prec[-1] + self.recall[-1] + 1e-8))
        self.acc.append((self.tp + self.tn) / (self.tp + self.fp + self.tn + self.fn))
    
    def get(self, key):
        return np.mean(getattr(self, key))

def printBar(i, total, prefix='\r', postfix=''):
    c = '='
    bar_length = 20
    progress = int(bar_length * i / total)
    bar = c * progress + '-' * (bar_length - progress)
    print(f'{prefix:>11}[{bar}] {i:4d}/{total:4d} | {postfix}', end='')

if __name__ == "__main__":
    datasetLoader, _, _ = split_dataset()
    lens = []
    for d in datasetLoader:
        lens += [len(x) for x in d.x]
    
    print(f"Max length: {max(lens)}")
    print(f"Min length: {min(lens)}")
    print(f"Mean length: {np.mean(lens)}")
    print(f"Median length: {np.median(lens)}")