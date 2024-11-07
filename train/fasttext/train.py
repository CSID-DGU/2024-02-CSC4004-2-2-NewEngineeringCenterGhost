import os
import sys
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

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
sample_size = 500000
dataset = dataset["train"].shuffle().select(range(sample_size))

for i, data in enumerate(dataset):
    text: str = data["text"]
    temp_sentences = nltk.sent_tokenize(text)
    if len(temp_sentences) == 0:
        continue
    if temp_sentences[-1][-1] == ".":
        temp_sentences[-1] = temp_sentences[-1][:-1]

    for temp_sentence in temp_sentences:
        sentence = m.morphs(temp_sentence + ".")
        sentence = [decomposeHangulText(word) for word in sentence]
        sentences.append(sentence)

    print(f"\rProcessing... ({i+1:7d}/{sample_size:7d})", end="")
print()
dataset = None

# load data/data.jsonl
print(f"\nTotal sentences: {len(sentences)}")

# FastText 모델 학습
model = FastText(sentences, vector_size=256, max_vocab_size=30000, min_count=1, workers=8, epochs=10)
model.save("train/fasttext/model/fasttext")
print("Model saved")

print(f"Model vocab size: {len(model.wv)}")