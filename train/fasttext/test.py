import os
import sys
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from gensim.models import FastText
from module.HGCtrlr import decomposeHangulText, composeHangulText

model = FastText.load("train/fasttext/model/fasttext")

test_words = [
    '스마트폰', '스맡폰', 'ㅏㅣ폰', '아ㅣㅇ폰',
    '햄부기', '스파게ㅌ', '한국어', '프랑스'
]

for word in test_words:
    print(f"\nWord: {word}")
    print("Similar words:")
    for similar_word in model.wv.most_similar(decomposeHangulText(word), topn=5):
        print(f"\t{composeHangulText(similar_word[0])} ({similar_word[1]:.4f})")