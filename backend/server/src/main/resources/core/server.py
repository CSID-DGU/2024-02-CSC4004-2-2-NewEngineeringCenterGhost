import os
bin_path = os.path.dirname(os.path.abspath(__file__)) + "/bin"

import torch
from gensim.models import FastText
from konlpy.tag import Mecab
import numpy as np
from module.custom_model import Model
from module.HGCtrlr import decomposeHangulText
import kss

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
m = Mecab()
fasttext = FastText.load(f"{bin_path}/fasttext")
model = Model().to(device)
# best model test f1 score:: 0.9236
model.load_state_dict(torch.load(f"{bin_path}/model_best.pth", weights_only=True))
model.eval()
model.set_return_att(True)

def get_important_tokens(atts) -> list:
    seq_len = atts[0].shape[-1]
    rollout = torch.eye(seq_len, device=device)

    for att in atts:
        att = att.view(seq_len, -1) + torch.eye(seq_len, device=device)
        att = att / att.sum(dim=-1, keepdim=True)
        rollout = torch.matmul(rollout, att)
    
    last_token = rollout[0].cpu().detach().numpy()

    # 상위 5% attention score를 가진 토큰 index를 반환
    _mean = np.mean(last_token)
    _std = np.std(last_token)
    z_score = (last_token - _mean) / _std
    rt = [i for i, z in enumerate(z_score) if z >= 1.96]

    if len(rt) == 0:
        rt = [np.argmax(last_token)]

    return rt

def get_sentence(tokens, ind):
    seps = [i for i, token in enumerate(tokens) if token == "SEP"]
    # find nearest SEP
    begin = 0
    end = 0
    for i in seps:
        if i > ind:
            end = i
            break
        begin = i
    
    return tokens[begin+1:end]

def predict(text):
    if not text.startswith("CLS"):
        text = [x.replace('\n', ' ').replace('  ', ' ').strip() for x in kss.split_sentences(text)]
        text = "CLS " + " SEP ".join(text) + " SEP"
    
    tokens = m.morphs(text)
    x = np.array([fasttext.wv[decomposeHangulText(word)] for word in tokens], dtype=np.float32)
    _len = len(x)
    padding_mask = np.zeros((_len,), dtype=np.bool_)
    y, atts = model(torch.tensor(x, device=device).view(1, _len, 256), torch.tensor(padding_mask, device=device).view(1, -1))

    sentences = []
    for ind in get_important_tokens(atts):
        sentence = "".join(get_sentence(tokens, ind))
        if len(sentence) == 0:
            continue
        sentences.append(sentence)
    
    sentences = list(set(sentences))

    return torch.sigmoid(y).item(), sentences

## Unix Domain Socket
import socket

socket_path = "/tmp/sock"

if os.path.exists(socket_path):
    os.remove(socket_path)

server = socket.socket(socket.AF_UNIX, socket.SOCK_STREAM)
server.bind(socket_path)

server.listen(5)

while True:
    conn, addr = server.accept()
    data = conn.recv(8192 * 4)
    data = data.decode(encoding='utf-8')
    res = predict(data)
    conn.send(str(res).encode())
    conn.close()