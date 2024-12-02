import os
bin_path = os.path.join(os.path.dirname(__file__), "..", "bin")

import torch
from gensim.models import FastText
from konlpy.tag import Mecab
import numpy as np
import kss
if __name__ == '__main__':
    from custom_model import Model
    from HGCtrlr import decomposeHangulText
else:
    from module.custom_model import Model
    from module.HGCtrlr import decomposeHangulText

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
class Seeker():
    def __init__(self):
        self.m = Mecab()
        self.fasttext = FastText.load(f"{bin_path}/fasttext")
        self.model = Model().to(device)
        self.model.load_state_dict(torch.load(f"{bin_path}/model_best.pth", weights_only=True))
        self.model.eval()
        self.model.set_return_att(True)
    
    def get_important_tokens(self, atts) -> list:
        seq_len = atts[0].shape[-1]
        rollout = torch.eye(seq_len, device=device)

        for att in atts:
            att = att.view(seq_len, -1) + torch.eye(seq_len, device=device)
            att = att / att.sum(dim=-1, keepdim=True)
            rollout = torch.matmul(rollout, att)
        
        last_token = rollout[0].cpu().detach().numpy()

        _mean = np.mean(last_token)
        _std = np.std(last_token)
        z_score = (last_token - _mean) / _std
        rt = [(i, z) for i, z in enumerate(z_score) if z >= 1.96]
        rt = rt.sort(key=lambda x: x[1], reverse=True)
        rt = [x[0] for x in rt][:5]

        if len(rt) == 0:
            rt = [np.argmax(last_token)]

        return rt
    
    def get_sentence(self, tokens, ind):
        seps = [i for i, token in enumerate(tokens) if token == "SEP"]
        begin = 0
        end = 0
        for i in seps:
            if i >= ind:
                end = i
                break
            begin = i
        
        return tokens[begin+1:end]
    
    def tokenize(self, text):
        if not text.startswith("CLS"):
            text = [x.replace('\n', ' ').replace('  ', ' ').strip() for x in kss.split_sentences(text)]
            text = "CLS " + " SEP ".join(text) + " SEP"

        tokens = self.m.morphs(text)
        return tokens
    
    def predict(self, text):
        tokens = self.tokenize(text)
        x = np.array([self.fasttext.wv[decomposeHangulText(token)] for token in tokens])
        y, atts = self.model(torch.tensor(x, device=device).unsqueeze(0))

        sentences = []
        for ind in self.get_important_tokens(atts):
            sentence = "".join(self.get_sentence(tokens, ind))
            if len(sentence) == 0:
                continue
            sentences.append(sentence)

        sentences = list(set(sentences))

        del x, atts
        torch.cuda.empty_cache()
        return torch.sigmoid(y).item(), sentences