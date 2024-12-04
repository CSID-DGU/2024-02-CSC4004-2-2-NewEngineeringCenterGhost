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

## 📖 **프로젝트 소개**  

![Clickbait Icon](frontend/ico/512.png)  

- **문제점**  
  인터넷상의 낚시성 정보는 사용자에게 혼란을 주고 시간 낭비를 초래합니다.  

- **해결책**  
  신뢰도가 낮은 콘텐츠를 사전에 구별하여 사용자가 효율적으로 정보를 탐색할 수 있도록 돕는 서비스입니다.  

- **기대효과**  
  정보 소비의 질을 높이고 신뢰성 높은 정보 문화를 확산하여 다양한 분야에서 활용 가능성을 기대합니다.  

---

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

### 2️⃣ **정밀 측정 기능**  
- 기사 또는 게시글 페이지에서 '측정하기'를 클릭하면 분석 결과를 상단 배너로 표시합니다.  
- 확률에 따라 배너 색상이 초록색(낮음) 또는 빨간색(높음)으로 나타납니다.  
- 네이버 및 구글에서 검색된 기사, 네이버 블로그, 티스토리, 인스타그램에서 사용 가능합니다.

### 3️⃣ **사용자 정의 측정 기능**  
- 사용자가 선택한 텍스트나 이미지를 분석하여 결과를 시각적으로 강조합니다.  
- 정밀 측정 기능에서 지원되지 않는 페이지에서 사용할 수 있도록 도와주는 기능입니다.

---

## 🛠 **프로젝트 아키텍처**  

![프로젝트 구조도](https://github.com/user-attachments/assets/d71885b9-5feb-4124-befd-336760140848)  

1. **AI 프레임워크**  
   - PyTorch를 기반으로 AI 모델을 학습 및 추론.  
   - 데이터셋: AI Hub 활용.  

2. **백엔드 서비스**  
   - Spring Boot 기반.  

3. **브라우저**  
   - Chrome Extension으로 콘텐츠를 처리.  

4. **Tesseract OCR**  
   - 이미지에서 텍스트 추출 후 AI 모델로 분석.  

---

## 🔧 **스킬 스택**  
### **OS** 
<img src="https://img.shields.io/badge/Linux-FCC624?style=flat-square&logo=Linux&logoColor=white"> <img src="https://img.shields.io/badge/Ubuntu-E95420?style=flat-square&logo=Ubuntu&logoColor=white">

### **IDE**  
<img src="https://img.shields.io/badge/Visual Studio Code-007ACC?style=flat-square&logo=Visual Studio Code&logoColor=white"> <img src="https://img.shields.io/badge/Intellij IDEA-000000?style=flat-square&logo=Intellij IDEA&logoColor=white">

### **Server**  
<img src="https://img.shields.io/badge/MongoDB-47A248?style=flat-square&logo=MongoDB&logoColor=white"> <img src="https://img.shields.io/badge/Selenium-43B02A?style=flat-square&logo=Selenium&logoColor=white">

### **Backend** 
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white">

### **Frontend**
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=JavaScript&logoColor=white"> <img src="https://img.shields.io/badge/JSON-000000?style=flat-square&logo=JSON&logoColor=white"> <img src="https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=HTML5&logoColor=white"> <img src="https://img.shields.io/badge/CSS3-1572B6?style=flat-square&logo=CSS3&logoColor=white">

### **AI**  
<img src="https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=Python&logoColor=white"> <img src="https://img.shields.io/badge/PyTorch-EE4C2C?style=flat-square&logo=PyTorch&logoColor=white"> <img src="https://img.shields.io/badge/OpenAI-412991?style=flat-square&logo=OpenAI&logoColor=white"> <img src="https://img.shields.io/badge/Tesseract OCR-5C3EE8?style=flat-square&logo=Tesseract&logoColor=white">

---  

## 📋 **라이센스 정보**  

### **프로젝트 라이센스**  
이 프로젝트는 **MIT License**를 따릅니다. 자세한 내용은 [LICENSE 파일](./LICENSE)을 확인하세요.  

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
- **링크**:  

---

## 🌐 **배포 URL**  
- **크롬 스토어**:  


---

## 🚀 **설치 방법**  

### 1️⃣ **Git으로 프로젝트 다운로드**  

Git이 없으면 [Git 다운로드](https://git-scm.com/downloads)에서 설치.


터미널에서 원하는 폴더로 이동 후 아래 명령어 입력

```bash  
git clone <프로젝트_링크>  
cd <프로젝트_폴더_이름>  
``` 

### 2️⃣ **JDK 설치 및 설정**  
- JDK 17 이상 설치 후 환경변수 설정.  

[Oracle JDK 다운로드](https://www.oracle.com/java/technologies/downloads/?er=221886)

[OpenJDK 다운로드](https://openjdk.org/)

JDK 17 이상 버전을 다운로드한 뒤 설치하세요.

### 3️⃣ **Python 패키지 설치**  
- Python 3.11 이상을 권장합니다.

```bash  
pip install -r requirements.txt  
```  

### 4️⃣ **프로젝트 빌드 및 실행**  
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
python3 unzip.py
```
위 명령어로 `train/data/data.jsonl`이 생성됩니다.

```bash
python3 train.py
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