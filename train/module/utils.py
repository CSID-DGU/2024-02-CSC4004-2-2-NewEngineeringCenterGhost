from torch.utils.data import Dataset
import numpy as np

class SeqDataset(Dataset):
    def __init__(self, x, y, max_len):
        self.x: np.ndarray = x
        self.y: np.ndarray = y
        self.max_len: int = max_len

    def __len__(self):
        return len(self.x)
    
    def __getitem__(self, idx):
        padding_mask = np.zeros((self.max_len), dtype=np.bool_)
        _len = len(self.x[idx])
        padding_mask[_len:] = True
        return (np.pad(self.x[idx], ((0, self.max_len - _len), (0, 0)), mode='constant'), padding_mask, self.y[idx])

class BigDataset(Dataset):
    def __init__(self, paths, max_len):
        self.paths: list = paths
        self.max_len: int = max_len

    def __len__(self):
        return len(self.paths)
    
    def __getitem__(self, idx):
        path = self.paths[idx]
        x = np.load(path + "_x.npy")
        y = np.load(path + "_y.npy")
        return SeqDataset(x, y, self.max_len)

def process_bar(i, total, loss, metrics):
    c = '='
    bar_length = 40
    progress = int(bar_length * i / total)
    bar = c * progress + '-' * (bar_length - progress)
    print(f'\r|{bar}| {i}/{total} Loss: {loss:.4f} Metrics: {metrics:.4f}', end='')