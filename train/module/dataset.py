from torch.utils.data import Dataset
from sklearn.model_selection import train_test_split
import numpy as np
import pandas as pd
from gensim.models import FastText
from konlpy.tag import Mecab
if __name__ == '__main__':
    from HGCtrlr import decomposeHangulText
else:
    from module.HGCtrlr import decomposeHangulText

mecab = Mecab()
fasttext = FastText.load("train/fasttext/model/fasttext")

def preprocess(text: str, max_len: int) -> tuple:
    x = mecab.morphs(text)
    x = np.array([fasttext.wv[decomposeHangulText(word)] for word in x], dtype=np.float32)
    padding_mask = np.zeros((max_len), dtype=np.bool_)
    _len = len(x)
    padding_mask[_len:] = True
    x = np.pad(x, ((0, max_len - _len), (0, 0)), mode='constant')
    return x, padding_mask

class SeqDataset(Dataset):
    def __init__(self, x, y, max_len):
        self.x = x
        self.y = y.astype(np.float32)
        self.max_len: int = max_len

    def __len__(self):
        return len(self.x)
    
    def __getitem__(self, idx):
        x, padding_mask = preprocess(self.x[idx], self.max_len)
        return x, padding_mask, self.y[idx]

def split_dataset(train_size=0.9, max_len=3584):
    dataset_dir = "train/data/data.jsonl"
    df = pd.read_json(dataset_dir, lines=True)
    X = df['content'].values
    y = df['label'].values
    X_train, X_val, y_train, y_val = train_test_split(X, y, train_size=train_size, stratify=y, random_state=42)
    X_val, X_test, y_val, y_test = train_test_split(X_val, y_val, train_size=0.5, stratify=y_val, random_state=42)
    return SeqDataset(X_train, y_train, max_len), SeqDataset(X_val, y_val, max_len), SeqDataset(X_test, y_test, max_len)

if __name__ == '__main__':
    train_dataset, val_dataset, test_dataset = split_dataset()
    print(len(train_dataset), len(val_dataset), len(test_dataset))
    print(val_dataset[-1])