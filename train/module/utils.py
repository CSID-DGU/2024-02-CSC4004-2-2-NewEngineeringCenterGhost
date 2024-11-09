import os
from torch.utils.data import Dataset
import torch.functional as F
import numpy as np

class SeqDataset(Dataset):
    def __init__(self, x, y, max_len):
        self.x = x
        self.y: np.ndarray = y.astype(np.float16)
        self.max_len: int = max_len

    def __len__(self):
        return len(self.x)
    
    def __getitem__(self, idx):
        padding_mask = np.zeros((self.max_len), dtype=np.bool_)
        _len = len(self.x[idx])
        padding_mask[_len:] = True
        return (np.pad(self.x[idx], ((0, self.max_len - _len), (0, 0)), mode='constant'), padding_mask, self.y[idx])

class DatasetLoader():
    def __init__(self, max_len):
        self.dataset_dir = "train/data/dataset"
        self.prefix = "dataset_"
        self.max_len = max_len
        self.len = len(os.listdir(self.dataset_dir)) // 2
        self.X = []
        self.y = []
        for i in range(self.len):
            self.X.append(f'{self.dataset_dir}/{self.prefix}{i:02d}_X.npz')
            self.y.append(f'{self.dataset_dir}/{self.prefix}{i:02d}_y.npy')

    def shuffle(self):
        idx = np.random.permutation(self.len)
        self.X = [self.X[i] for i in idx]
        self.y = [self.y[i] for i in idx]

    def __len__(self):
        return self.len
    
    def __getitem__(self, idx):
        _X = list(np.load(self.X[idx]).values())
        _y = np.load(self.y[idx])
        return SeqDataset(_X, _y, self.max_len)

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
    format_length = len(str(total))
    print(f'{prefix}[{bar}] {i:{format_length}d}/{total} | {postfix}', end='')

if __name__ == "__main__":
    datasetLoader = DatasetLoader(max_len=3584)
    lens = []
    for d in datasetLoader:
        lens += [len(x) for x in d.x]
    
    print(f"Max length: {max(lens)}")
    print(f"Min length: {min(lens)}")
    print(f"Mean length: {np.mean(lens)}")
    print(f"Median length: {np.median(lens)}")