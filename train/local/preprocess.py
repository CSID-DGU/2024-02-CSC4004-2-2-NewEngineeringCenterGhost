import os
import pandas as pd
import numpy as np
from kobert_tokenizer import KoBERTTokenizer
from sklearn.model_selection import train_test_split

tokenizer = KoBERTTokenizer.from_pretrained('skt/kobert-base-v1')

data_path = './train/local/data/data.jsonl'
save_path = './train/local/data/npy'

data = pd.read_json(data_path, lines=True)
X = np.arange(len(data))
y = data['label'].values

dX = {}
dy = {}
dX['train'], dX['test'], dy['train'], dy['test'] = train_test_split(X, y, test_size=0.2, stratify=y)
dX['test'], dX['val'], dy['test'], dy['val'] = train_test_split(dX['test'], dy['test'], test_size=0.5, stratify=dy['test'])

for split in ['train', 'val', 'test']:
    # 8192개씩 나눠서 저장
    for i in range(len(dX[split])//8192 + 1):
        start = i*8192
        end = min((i+1)*8192, len(dX[split]))
        if start == end:
            break
        X = data['sentence'].iloc[dX[split][start:end]].values
        y = dy[split][start:end]
        X = [tokenizer.encode(x, max_length=128, pad_to_max_length=True) for x in X]
        X = np.array(X)
        np.save(os.path.join(save_path, f'{split}_{i}.npy'), X)
        np.save(os.path.join(save_path, f'{split}_{i}_label.npy'), y)