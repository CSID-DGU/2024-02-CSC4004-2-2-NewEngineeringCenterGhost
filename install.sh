#cli: bash install.sh

# MeCab 및 tesseract 설치
sudo apt-get update
sudo apt-get install -y g++ openjdk-8-jdk python3-dev python3-pip curl git
sudo apt install tesseract-ocr
sudo apt install libtesseract-dev
sudo apt install tesseract-ocr-kor
sudo apt install tesseract-ocr-eng

pip install --upgrade pip

pip install konlpy
pip install datasets
pip install mecab-python3
pip install tesseract

bash <(curl -s https://raw.githubusercontent.com/konlpy/konlpy/master/scripts/mecab.sh)
curl -s https://raw.githubusercontent.com/teddylee777/machine-learning/master/99-Misc/01-Colab/mecab-colab.sh | bash
