import os
import sys
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

import torch
from gensim.models import FastText
from konlpy.tag import Mecab
import numpy as np
from module.custom_model import Model
from module.HGCtrlr import decomposeHangulText

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
m = Mecab()
fasttext = FastText.load("bin/fasttext")
model = Model().to(device)
model.load_state_dict(torch.load("bin/model_val_f1_76.pth", weights_only=True))

def predict(text):
    x = m.morphs(text)
    x = np.array([fasttext.wv[decomposeHangulText(word)] for word in x], dtype=np.float32)
    padding_mask = np.zeros((3584,), dtype=np.bool_)
    _len = len(x)
    padding_mask[_len:] = True
    x = np.pad(x, ((0, 3584 - _len), (0, 0)), mode='constant')
    y = model(torch.tensor(x, device=device).view(1, 3584, 256), torch.tensor(padding_mask, device=device).view(1, -1))

    return torch.nn.functional.sigmoid(y).item()