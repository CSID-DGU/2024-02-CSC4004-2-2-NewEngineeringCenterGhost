import os
import pandas as pd
import numpy as np
import torch
from gensim.models import FastText
from konlpy.tag import Mecab
from module.HGCtrlr import decomposeHangulText

m = Mecab()
model = FastText.load("train/fasttext/model/fasttext")
dataset = pd.read_json("train/data/data.jsonl", lines=True) # content, label
dataset = dataset.sample(frac=1).reset_index(drop=True) # shuffle

# 데이터 전처리
print("\nPreprocessing data...")
for i in range(0, len(dataset), 8192):
    X = dict()
    for j, x in enumerate(dataset['content'][i:i+8192]):
        print(f"\rProcessing... {i+j+1:6d}/{len(dataset)}", end="")
        x = m.morphs(x)
        x = np.array([model.wv[decomposeHangulText(word)] for word in x], dtype=np.float32)
        X[str(j)] = x
    np.savez_compressed(f"train/data/dataset/{i//8192:02d}_X.npz", **X)
    
    y = dataset['label'][i:i+8192].values.astype(np.bool_)
    np.save(f"train/data/dataset/{i//8192:02d}_y.npy", y)

print("\nDone!")