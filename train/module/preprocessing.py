import torch
import numpy as np
from gensim.models import FastText
from konlpy.tag import Mecab
if __name__ == '__main__':
    from HGCtrlr import decomposeHangulText
else:
    from module.HGCtrlr import decomposeHangulText

m = Mecab()
fasttext = FastText.load("train/fasttext/model/fasttext")

def preprocess(text: str, max_len: int) -> tuple:
    x = m.morphs(text)
    x = np.array([fasttext.wv[decomposeHangulText(word)] for word in x], dtype=np.float32)
    padding_mask = np.zeros((max_len), dtype=np.bool_)
    _len = len(x)
    padding_mask[_len:] = True
    x = np.pad(x, ((0, max_len - _len), (0, 0)), mode='constant')
    return x, padding_mask

if __name__ == "__main__":
    text = "안녕하세요"
    x, padding_mask = preprocess(text, 128)
    print(x.shape, padding_mask.shape)
    print(x.dtype, padding_mask.dtype)
    print(x)
    print(padding_mask)