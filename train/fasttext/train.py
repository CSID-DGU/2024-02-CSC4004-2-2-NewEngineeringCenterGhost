import os
import sys
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from gensim.models import FastText
from konlpy.tag import Mecab
from datasets import load_dataset
from module.HGCtrlr import decomposeHangulText

m = Mecab()

# HAERAE-HUB/KOREAN-WEBTEXT 데이터셋 전처리
sentences = []
print("\nLoading HAERAE-HUB/KOREAN-WEBTEXT...")
dataset = load_dataset("HAERAE-HUB/KOREAN-WEBTEXT")
total_samples = len(dataset["train"])
for i, data in enumerate(dataset["train"]):
    text: str = data["text"]
    temp_sentences = list(filter(lambda x: len(x) > 0, text.split(". ")))
    if len(temp_sentences) == 0:
        continue
    if temp_sentences[-1][-1] == ".":
        temp_sentences[-1] = temp_sentences[-1][:-1]

    for temp_sentence in temp_sentences:
        sentence = m.morphs(temp_sentence + ".")
        sentence = [decomposeHangulText(word) for word in sentence]
        sentences.append(sentence)

    print(f"\rProcessing... ({i+1:8d}/{total_samples:8d})", end="")
print()
dataset = None

# load data/data.jsonl
print(f"\nTotal sentences: {len(sentences)}")

# FastText 모델 학습
model = FastText(sentences, vector_size=256, max_vocab_size=30000, min_count=1, workers=8, epochs=10)
model.save("train/fasttext/model/fasttext")
print("Model saved")

print(f"Model vocab size: {len(model.wv)}")