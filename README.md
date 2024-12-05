### 📜 README.md  

---

# **2024-02-CSC4004-2-2-NewEngineeringCenterGhost**  
👻 CSC4004 공개 SW 프로젝트 2조 **신공학관 지박령**입니다 👻  

---

## 👩‍💻 **팀원 소개**  

| 이름       | 학번       | 역할                     | GitHub                                   |  
|------------|------------|--------------------------|------------------------------------------|  
| **정재환** | 2020112023 | 팀장, 백엔드             | [@hwnnn](https://github.com/hwnnn)      |  
| **신동주** | 2020112541 | AI                       | [@smturtle2](https://github.com/smturtle2) |  
| **정태호** | 2022113556 | 크롬 확장 프로그램, 프론트엔드 | [@Taehoo-Jeong](https://github.com/Taehoo-Jeong) |  
| **조성원** | 2020112037 | AI                       | [@sungwon01](https://github.com/sungwon01) |  
| **임상수** | | 담당 교수                       |  |  

---

## 📖 **프로젝트: 낚시성 정보 탐지 도구 Click-bait Seeker**  
![Clickbait Icon](frontend/ico/512.png)  

---

### 🔍 **문제점**  
인터넷에는 낚시성 정보가 넘쳐나 사용자들의 시간 낭비와 혼란을 초래하고 있습니다. 이러한 콘텐츠는 신뢰할 수 있는 정보에 접근하는 것을 어렵게 만들며, 잘못된 정보의 확산으로 사회적 오해를 불러일으킬 수 있습니다.

### 🛠️ **해결책**  
본 확장 프로그램은 텍스트와 이미지를 분석하여 콘텐츠의 낚시성 확률을 계산하고, 주요 문장과 함께 해설을 제공합니다. 이를 통해 사용자가 신뢰성 있는 정보를 빠르게 탐색할 수 있도록 돕습니다.

### ✨ **기대효과**  
1️⃣ **시간 절약**: 낚시성 콘텐츠를 사전에 식별하여 효율적인 정보 탐색을 지원합니다.  
2️⃣ **정보 이해도 향상**: 주요 문장과 해설을 통해 콘텐츠의 신뢰도를 직관적으로 이해하도록 돕습니다.  
3️⃣ **인터넷 환경 개선**: 신뢰성 높은 콘텐츠 소비를 장려해 건강한 정보 문화를 확산시킵니다.  

---

숫자 이모티콘으로 시각적 강조를 더하면서도 깔끔함을 유지했습니다! 😊

## 📂 **프로젝트 파일 구조**  

### 🛠️ **Backend**
```
backend  
 ┗ server  
     ┣ gradle  
     ┣ src/main/java/com/newengineeringghost  
     ┃   ┣ domain/api/controller/ApiController.java  
     ┃   ┣ domain/api/dto/*.java  
     ┃   ┣ domain/api/entity/*.java  
     ┃   ┣ domain/api/repository/*.java  
     ┃   ┗ domain/api/service/ApiService.java  
     ┗ src/main/resources  
         ┣ core/*.py  
         ┣ ocr/TestOCR.py  
         ┗ openai/TestOpenAi.py  
```  

### 💻 **Frontend**
```
frontend  
 ┣ background.js  
 ┣ content.js  
 ┣ highlight.js  
 ┣ manifest.json 
 ┣ markdown.js   
 ┣ popup.css  
 ┣ popup.html  
 ┣ popup.js  
 ┗ styles.css  
```  

### 🧠 **Train**
```
train  
 ┣ fasttext/*.py  
 ┣ module/*.py  
 ┣ model_best.pth  
 ┣ train.py  
 ┗ unzip.py  
```  

---  

## ✨ **기능 소개**  

### 1️⃣ **빠른 측정 기능**  
- 링크 위에 마우스를 올리면, 해당 기사 또는 게시글의 낚시성 확률을 실시간으로 표시합니다.  
![스크린샷 2024-12-04 221110](https://github.com/user-attachments/assets/4860d8e5-5fa2-485f-a2d6-cb617bbb4882)

### 2️⃣ **정밀 측정 기능**  
- 기사 또는 게시글 페이지에서 '측정하기'를 클릭하면 분석 결과를 상단 배너로 표시합니다.  
- 확률에 따라 배너 색상이 초록색(낮음) 또는 빨간색(높음)으로 나타납니다.  
- 네이버 및 구글에서 검색된 기사, 네이버 블로그, 티스토리, 인스타그램에서 사용 가능합니다.  
![스크린샷 2024-12-04 220710](https://github.com/user-attachments/assets/3774c102-f7b3-4794-a91a-08818b111936)


### 3️⃣ **사용자 정의 측정 기능**  
- 사용자가 선택한 텍스트나 이미지를 분석하여 결과를 시각적으로 강조합니다.  
- 정밀 측정 기능에서 지원되지 않는 페이지에서 사용할 수 있도록 도와주는 기능입니다.  
![스크린샷 2024-12-04 221235](https://github.com/user-attachments/assets/c308e71d-a3ee-41c9-86fe-e436744c5dd3)


---

## 🛠 **프로젝트 아키텍처**  

![프로젝트 구조도](https://github.com/user-attachments/assets/d71885b9-5feb-4124-befd-336760140848)  

### **1. AI 분석 시스템**  
- 텍스트 콘텐츠를 **PyTorch** 기반 AI 모델로 분석하여 낚시성 정보가 포함될 확률을 계산합니다.  
- 확률이 50% 이상인 경우, 주요 문장을 추출하고, **OpenAI GPT**를 사용해 해당 콘텐츠에 대한 해설을 생성합니다.  
- 텍스트 중간에 이미지가 포함된 경우, **Tesseract OCR**을 활용해 이미지에서 텍스트를 추출하여 추가로 분석합니다.  

### **2. 백엔드 서비스**  
- **Spring Boot** 기반 서버가 AI 분석 시스템과 브라우저 확장 프로그램 간 데이터를 처리하며, 분석 결과와 해설을 프론트엔드로 전달합니다.  

### **3. 브라우저 확장 프로그램**  
- **Chrome Extension**을 통해 콘텐츠의 낚시성 확률, 주요 문장, 그리고 해설을 실시간으로 시각화합니다.  
- 사용자가 선택한 텍스트나 이미지에 대해 AI 분석 결과를 제공하며, 필요한 경우 추가 해설을 표시합니다.  

---

## 🔧 **스킬 스택**  
### **OS** 
<img src="https://img.shields.io/badge/Linux-FCC624?style=flat-square&logo=Linux&logoColor=white"> <img src="https://img.shields.io/badge/Ubuntu-E95420?style=flat-square&logo=Ubuntu&logoColor=white">

### **IDE**  
<img src="https://img.shields.io/badge/Visual Studio Code-007ACC?style=flat-square&logo=Visual Studio Code&logoColor=white"> <img src="https://img.shields.io/badge/Intellij IDEA-000000?style=flat-square&logo=Intellij IDEA&logoColor=white">

### **Backend** 
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white"> <img src="https://img.shields.io/badge/MongoDB-47A248?style=flat-square&logo=MongoDB&logoColor=white"> <img src="https://img.shields.io/badge/Selenium-43B02A?style=flat-square&logo=Selenium&logoColor=white">

### **Frontend**
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=JavaScript&logoColor=white"> <img src="https://img.shields.io/badge/JSON-000000?style=flat-square&logo=JSON&logoColor=white"> <img src="https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=HTML5&logoColor=white"> <img src="https://img.shields.io/badge/CSS3-1572B6?style=flat-square&logo=CSS3&logoColor=white">

### **AI**  
<img src="https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=Python&logoColor=white"> <img src="https://img.shields.io/badge/PyTorch-EE4C2C?style=flat-square&logo=PyTorch&logoColor=white"> <img src="https://img.shields.io/badge/OpenAI-412991?style=flat-square&logo=OpenAI&logoColor=white"> <img src="https://img.shields.io/badge/Tesseract OCR-5C3EE8?style=flat-square&logo=Tesseract&logoColor=white">

---  

## 📋 **라이센스 정보**  

### **프로젝트 라이센스**  
<img src="https://img.shields.io/badge/Click--bait%20Seeker-1.0.0%20(MIT%20License)-blueviolet?style=flat-square">

이 프로젝트는 **MIT License**를 따릅니다. 자세한 내용은 [LICENSE 파일](./LICENSE)을 확인하세요.  
또한, **PyTorch**의 일부 코드를 수정하여 포함하고 있습니다. 해당 코드는 **BSD 3-Clause License**의 조건을 따릅니다. 자세한 내용은 [PyTorch NOTICE](https://github.com/pytorch/pytorch/blob/main/NOTICE)를 확인하세요.


### **사용된 OSS 목록:**  
[<img src="https://img.shields.io/badge/Spring%20Boot-3.0.x%20(Apache%202.0)-6DB33F?style=flat-square&logo=Spring%20Boot&logoColor=white">](https://spring.io/projects/spring-boot)
[<img src="https://img.shields.io/badge/PyTorch-2.5.1%20(BSD--3--Clause)-EE4C2C?style=flat-square&logo=PyTorch&logoColor=white">](https://pytorch.org/)
[<img src="https://img.shields.io/badge/Gensim-4.3.1%20(LGPL%20v2.1)-0078D4?style=flat-square">](https://radimrehurek.com/gensim/)
[<img src="https://img.shields.io/badge/Tesseract%20OCR-5.x%20(Apache%202.0)-5C3EE8?style=flat-square&logo=Tesseract&logoColor=white">](https://github.com/tesseract-ocr/tesseract)
[<img src="https://img.shields.io/badge/NLTK-3.9.1%20(Apache%202.0)-39A85E?style=flat-square&logo=NLTK&logoColor=white">](https://github.com/nltk/nltk)
[<img src="https://img.shields.io/badge/KSS-6.0.4%20(BSD--3--Clause)-FF4500?style=flat-square&logo=KSS&logoColor=white">](https://github.com/hyunwoongko/kss)
[<img src="https://img.shields.io/badge/Selenium-4.x%20(Apache%202.0)-43B02A?style=flat-square&logo=Selenium&logoColor=white">](https://www.selenium.dev/)


---

## 🎥 **시연 영상**  
[![Video Label](https://img.youtube.com/vi/gFVVAIdI-GQ/0.jpg)](https://www.youtube.com/watch?v=gFVVAIdI-GQ)  

---

## 🌐 **배포 URL**  
- **크롬 스토어**:  


---

## 🚀 **설치 방법**  

### 1️⃣ **Git으로 프로젝트 다운로드**  

Git이 없으면 [Git 다운로드](https://git-scm.com/downloads)에서 설치.

터미널에서 원하는 폴더로 이동 후 아래 명령어 입력:

```bash  
git clone <프로젝트_링크>  
cd <프로젝트_폴더_이름>  
``` 

### 2️⃣ **JDK 설치 및 설정**  
- JDK 17 이상 설치 후 환경변수 설정.  
[OpenJDK 다운로드](https://openjdk.org/)

JDK 17 이상 버전을 다운로드한 뒤 설치하세요.

### 3️⃣ **설치 스크립트 실행**  
터미널에서 프로젝트 폴더로 이동 후 아래 명령어 입력:

```bash
bash install.sh
```

### 4️⃣ **Python 패키지 설치**  
- Python 3.11 이상을 권장합니다.

```bash  
pip install -r requirements.txt  
```  

### 5️⃣ **프로젝트 빌드 및 실행**  
```bash  
./gradlew build  
java -jar build/libs/<생성된_jar파일명>.jar  
```  

---

## 📊 **AI 모델 학습**

### **FastText 모델 학습**  
```bash
cd train/fasttext
python3 train.py
```
학습된 모델은 `train/fasttext/model`에 저장됩니다.
해당 디렉토리 내의 모든 파일을 `backend/src/main/resources/core/bin`에 복사합니다.

### **Transformer 모델 학습**  
학습을 하기 전, [AI Hub 낚시성 정보 데이터](https://www.aihub.or.kr/aihubdata/data/view.do?currMenu=115&topMenu=100&dataSetSn=71338)를 다운로드하여 모든 라벨링 데이터 zip 파일을 `train/data/zip`에 저장합니다.

```bash
python3 train/unzip.py
```
위 명령어로 `train/data/data.jsonl`이 생성됩니다.

```bash
python3 train/train.py
```
모델 학습을 진행합니다. 컴퓨팅 자원이 부족할 수 있으니 주의하세요.

### **제공된 모델 사용**
GitHub에 모델 파일이 업로드되어 있습니다. FastText 모델만 학습시킨 후,
`train/model_best.pth`를 `backend/src/main/resources/core/bin`에 복사합니다.

---

### 📝 **문의**  
- **이메일**:
   - 정재환: 2020112023@dgu.ac.kr
   - 신동주: smturtle2@kakao.com
   - 정태호: t2222h@naver.com
   - 조성원: chosung01@naver.com
