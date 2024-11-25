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

def compute_attention_rollout(atts):
    rt = atts[0].squeeze(0)

    for i in range(1, len(atts)):
        rt = rt @ atts[i].squeeze(0)
        rt = rt / rt.max()
    
    rt = rt.mean(dim=0).view(1, -1)

    # plot
    if False:
        rt = torch.nn.functional.avg_pool1d(rt, 3, stride=1, padding=1)
        import matplotlib.pyplot as plt
        plt.figure(figsize=(10, 10))
        plt.imshow(rt.cpu().detach().numpy(), cmap='hot', interpolation='nearest')
        plt.show()

    rt = (rt / rt.max() > .9).squeeze(0)

    rt = rt.nonzero().squeeze(1).cpu().tolist()
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
    for ind in compute_attention_rollout(atts):
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