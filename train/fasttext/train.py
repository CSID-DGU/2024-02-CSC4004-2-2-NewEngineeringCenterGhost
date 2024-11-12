import os
import sys
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

import random
import multiprocessing
from gensim.models import FastText
from konlpy.tag import Mecab
from datasets import load_dataset
from module.HGCtrlr import decomposeHangulText
import nltk
nltk.download('punkt')

m = Mecab()
max_sentence_len = 12

# HAERAE-HUB/KOREAN-SyntheticText-1.5B 데이터셋 전처리
sentences = []
print("\nLoading HAERAE-HUB/KOREAN-SyntheticText-1.5B...")
dataset = load_dataset("HAERAE-HUB/KOREAN-SyntheticText-1.5B")
dataset = dataset["train"]

lens = []
for i, data in enumerate(dataset):
    text: str = data["text"]
    temp_sentences = (nltk.sent_tokenize(text))
    _len = len(temp_sentences)
    lens.append(_len)
    if _len == 0:
        continue
    
    if _len > max_sentence_len:
        temp_sentences = random.sample(temp_sentences, max_sentence_len)

    for temp_sentence in temp_sentences:
        sentence = ['<CLS>'] + m.morphs(temp_sentence) + ['<SEP>']
        sentence = [decomposeHangulText(word) for word in sentence]
        sentences.append(sentence)

    print(f"\rProcessing... ({i+1:7d}/{len(dataset)})", end="")
print()
dataset = None

# load data/data.jsonl
print(f"\nTotal sentences: {len(sentences)}")

lens = sorted(lens)
print(f"Max sentence count in a document: {lens[-1]}")
print(f"Min sentence count in a document: {lens[0]}")
print(f"Average sentence count in a document: {sum(lens) / len(lens)}")
print(f"Median sentence count in a document: {lens[len(lens) // 2]}")

# FastText 모델 학습
model = FastText(sentences, vector_size=256, max_vocab_size=30000, workers=multiprocessing.cpu_count())
model.save("train/fasttext/model/fasttext")
print("Model saved")

print(f"Model vocab size: {len(model.wv)}")