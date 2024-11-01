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

for k in ['train', 'val', 'test']:
    #데이터를 8192개씩 나눠서 저장
    print(f'{k} data save')
    for i, j in enumerate(range(0, len(dX[k]), 8192)):
        print(f'{i+1}/{len(range(0, len(dX[k]), 8192))}')
        X = data['sentence'].values[dX[k][j:j+8192]]
        y = np.array(dy[k][j:j+8192], dtype=np.bool)
        X = [tokenizer.encode(x, max_length=4096, padding='max_length', truncation=True) for x in X]
        X = np.array(X)
        np.save(f'{save_path}/{k}/X_{i}.npy', X)
        np.save(f'{save_path}/{k}/y_{i}.npy', y)

print('done')