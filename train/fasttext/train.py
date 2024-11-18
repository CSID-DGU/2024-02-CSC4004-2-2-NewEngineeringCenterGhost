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

sentences = []
for i, data in enumerate(dataset):
    text: str = data["text"]
    temp_sentences = (nltk.sent_tokenize(text))
    sentences.extend(temp_sentences)
    print(f"\rProcessing... ({i+1:7d}/{len(dataset)})", end="")
dataset = None

class SentenceDataset:
    def __init__(self, sentences):
        self.sentences = sentences

    def __iter__(self):
        for sentence in self.sentences:
            sentence = ['CLS'] + m.morphs(sentence) + ['SEP']
            yield [decomposeHangulText(word) for word in sentence]

# load data/data.jsonl
print(f"\nTotal sentences: {len(sentences)}")

sentences = SentenceDataset(sentences)

# FastText 모델 학습
model = FastText(sentences, vector_size=256, max_vocab_size=30000, workers=multiprocessing.cpu_count())
model.save("train/fasttext/model/fasttext")
print("Model saved")

print(f"Model vocab size: {len(model.wv)}")