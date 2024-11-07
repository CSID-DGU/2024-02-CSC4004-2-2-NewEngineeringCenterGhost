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

# HAERAE-HUB/KOREAN-SyntheticText-1.5B 데이터셋 전처리
sentences = []
print("\nLoading HAERAE-HUB/KOREAN-SyntheticText-1.5B...")
dataset = load_dataset("HAERAE-HUB/KOREAN-SyntheticText-1.5B")
dataset = dataset["train"]

for i, data in enumerate(dataset):
    text: str = data["text"]
    temp_sentences = (nltk.sent_tokenize(text))
    if len(temp_sentences) < 5:
        continue

    temp_sentences = random.sample(temp_sentences, 5)
    if temp_sentences[-1][-1] == ".":
        temp_sentences[-1] = temp_sentences[-1][:-1]

    for temp_sentence in temp_sentences:
        sentence = m.morphs(temp_sentence + ".")
        sentence = [decomposeHangulText(word) for word in sentence]
        sentences.append(sentence)

    print(f"\rProcessing... ({i+1:7d}/{len(dataset)})", end="")
print()
dataset = None

# load data/data.jsonl
print(f"\nTotal sentences: {len(sentences)}")

# FastText 모델 학습
model = FastText(sentences, vector_size=256, max_vocab_size=30000, workers=multiprocessing.cpu_count())
model.save("train/fasttext/model/fasttext")
print("Model saved")

print(f"Model vocab size: {len(model.wv)}")