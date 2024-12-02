# 2024-02-CSC4004-2-2-NewEngineeringCenterGhost
👻CSC4004-공개SW프로젝트 2조 신공학관 지박령입니다👻

## 팀원 소개
| 이름 | 학번 | 역할 | GITHUB |
| :---: | :---: | :---: | :---: |
| 정재환 | 2020112023 | 팀장, 백엔드 | [@hwnnn](https://github.com/hwnnn) |
| 신동주 | 2020112541 | AI | [@smturtle2](https://github.com/smturtle2) |
| 정태호 | 2022113556 | 크롬 확장 프로그램, 프론트엔드 | [@Taehoo-Jeong](https://github.com/Taehoo-Jeong) |
| 조성원 | 2020112037 | AI | [@sungwon01](https://github.com/sungwon01) |

## 프로젝트 소개
![clickbait](https://github.com/user-attachments/assets/fac60d88-49b3-4c4f-8884-53c9ec04426b)

- 인터넷상의 낚시성 정보로 인한 시간 낭비 문제와 사용자의 혼란를 해결하고자 시작한 프로젝트.
- 사용자가 신뢰도 낮은 콘텐츠를 사전에 구별하여, 정보를 효율적으로 탐색할 수 있도록 돕는 서비스. 
- 정보 소비의 질을 높이고, 신뢰성 높은 정보 문화를 확산시킴으로써 다양한 분야에서 활용 가능성 기대.



## 프로젝트 파일 구조
```
backend
 ┗ server
 ┃ ┣ .idea
 ┃ ┃ ┣ .gitignore
 ┃ ┃ ┣ dataSources.xml
 ┃ ┃ ┣ gradle.xml
 ┃ ┃ ┣ misc.xml
 ┃ ┃ ┗ vcs.xml
 ┃ ┣ gradle
 ┃ ┃ ┗ wrapper
 ┃ ┃ ┃ ┣ gradle-wrapper.jar
 ┃ ┃ ┃ ┗ gradle-wrapper.properties
 ┃ ┣ src
 ┃ ┃ ┗ main
 ┃ ┃ ┃ ┣ java
 ┃ ┃ ┃ ┃ ┗ com
 ┃ ┃ ┃ ┃ ┃ ┗ newengineeringghost
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ api
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ ApiController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ ResponseDataDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ WebContentsDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ ResponseData.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ ResponseDataRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ ApiService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ ServerApplication.java
 ┃ ┃ ┃ ┗ resources
 ┃ ┃ ┃ ┃ ┣ core
 ┃ ┃ ┃ ┃ ┃ ┣ module
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ HGCtrlr.py
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ custom_model.py
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ custom_transformer.py
 ┃ ┃ ┃ ┃ ┃ ┣ .gitignore
 ┃ ┃ ┃ ┃ ┃ ┣ request.py
 ┃ ┃ ┃ ┃ ┃ ┗ server.py
 ┃ ┃ ┃ ┃ ┣ ocr
 ┃ ┃ ┃ ┃ ┃ ┗ TestOCR.py
 ┃ ┃ ┃ ┃ ┣ openai
 ┃ ┃ ┃ ┃ ┃ ┗ TestOpenAi.py
 ┃ ┃ ┃ ┃ ┗ application.yml
 ┃ ┣ .gitignore
 ┃ ┣ build.gradle
 ┃ ┣ gradlew
 ┃ ┣ gradlew.bat
 ┃ ┗ settings.gradle
```

```
frontend
 ┣ background.js
 ┣ content.js
 ┣ manifest.json
 ┣ popup.html
 ┗ styles.css
```

```
train
 ┣ fasttext
 ┃ ┣ test.py
 ┃ ┗ train.py
 ┣ module
 ┃ ┣ HGCtrlr.py
 ┃ ┣ custom_model.py
 ┃ ┣ custom_transformer.py
 ┃ ┣ dataset.py
 ┃ ┣ preprocessing.py
 ┃ ┗ utils.py
 ┣ test.ipynb
 ┣ train.py
 ┗ unzip.py
```

## 기능 소개
- 인터넷 뉴스 기사에서 다음의 세 가지 방법을 통해 해당 정보가 낚시성 정보인지를 구체적인 수치와 낚시성 정보라고 판단되는 이유를 함께 제시한다.

  1. 빠른 측정 기능
    - 링크 위에 마우스를 올리면 서버에서 기사를 분석하여 낚시성 정보 확률을 링크 위에 표시한다.


  2. 정밀 측정 기능
    - 기사 페이지에서 '측정하기'를 선택하면 서버 분석 결과를 상단 배너로 표시하며, 낚시성 정보 확률이 높을 경우 배너 색상이 빨간색, 낮을 경우 초록색으로 나타난다.


  3. 사용자 정의 측정 기능
    - 사용자가 선택한 텍스트나 이미지를 임시 저장 후 '측정하기'를 통해 분석하며, 낚시성 정보 확률이 높은 텍스트는 빨갛게, 이미지는 빨간 테두리와 경고 이미지로 강조한다.

- 나아가 블로그, 카드뉴스 형식의 SNS 게시물에도 이를 적용 시킬 수 있다.

## 프로젝트 아키텍처
![프로젝트 구조도](https://github.com/user-attachments/assets/70e02ee4-391d-411e-943e-971636922fad)
이 프로젝트 아키텍처는 본 프로젝트의 전반적인 흐름을 나타낸다.

1. AI 프레임워크 
 - AI 모델 학습 및 추론에는 PyTorch를 사용한다.
 - 학습 데이터는 AI Hub에서 제공되는 데이터셋을 활용한다.

2. 백엔드 서비스
  - Spring Boot 기반 백엔드 역할을 한다.

3. 브라우저
  - 뉴스, 문서, 블로그 등의 콘텐츠를 처리하는 Chrome Extension이 프론트엔드 역할을 한다.

4. Tesseract OCR
  - 이미지에서 텍스트를 추출하기 위해 사용된다.
  - 추출된 텍스트는 AI 모델이 분석하거나 백엔드에 의해 처리된다.

## 스킬 스택
- OS : <img src="https://img.shields.io/badge/Linux-FCC624?style=flat-square&logo=Linux&logoColor=white"> <img src="https://img.shields.io/badge/Ubuntu-E95420?style=flat-square&logo=Ubuntu&logoColor=white">

- IDE : <img src="https://img.shields.io/badge/Visual Studio Code-007ACC?style=flat-square&logo=Visual Studio Code&logoColor=white"> <img src="https://img.shields.io/badge/Intellij IDEA-000000?style=flat-square&logo=Intellij IDEA&logoColor=white">

- Server : <img src="https://img.shields.io/badge/MongoDB-47A248?style=flat-square&logo=MongoDB&logoColor=white"> <img src="https://img.shields.io/badge/Selenium-43B02A?style=flat-square&logo=Selenium&logoColor=white">

- Backend : <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white">

- Frontend : <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=JavaScript&logoColor=white"> <img src="https://img.shields.io/badge/JSON-000000?style=flat-square&logo=JSON&logoColor=white"> <img src="https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=HTML5&logoColor=white"> <img src="https://img.shields.io/badge/CSS3-1572B6?style=flat-square&logo=CSS3&logoColor=white">

- AI : <img src="https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=Python&logoColor=white"> <img src="https://img.shields.io/badge/PyTorch-EE4C2C?style=flat-square&logo=PyTorch&logoColor=white"> <img src="https://img.shields.io/badge/OpenAI-412991?style=flat-square&logo=OpenAI&logoColor=white"> <img src="https://img.shields.io/badge/OpenCV-5C3EE8?style=flat-square&logo=OpenCV&logoColor=white">

## 시연 영상
- 링크 : 

## 배포 URL
- 배포 URL : 

## 설치 방법
1️⃣ Git으로 프로젝트 다운로드

Git이 없으면 [Git 다운로드](https://git-scm.com/downloads)에서 설치.


터미널에서 원하는 폴더로 이동 후 아래 명령어 입력

```
git clone <프로젝트_링크>
cd <프로젝트_폴더_이름>
```
2️⃣ JDK 설치 및 확인
1.JDK 다운로드 및 설치

[Oracle JDK 다운로드](https://www.oracle.com/java/technologies/downloads/?er=221886)

[OpenJDK 다운로드](https://openjdk.org/)

JDK 17 이상 버전을 다운로드한 뒤 설치하세요.

2.환경변수 설정

- Windows

시스템 속성 → 환경 변수 → JAVA_HOME에 JDK 설치 경로 추가.

- Mac/Linux

.bashrc 또는 .zshrc에 아래 내용 추가
```
export JAVA_HOME=/path/to/jdk

export PATH=$JAVA_HOME/bin:$PATH
```
3.설치 확인
```
java -version
```
3️⃣ Python 설치 및 패키지 설치

1.Python 설치

[Python 다운로드 페이지](https://www.python.org/downloads/)에서 Python 3.8 이상 버전 설치.

설치 시 "Add Python to PATH" 옵션을 체크하세요.

2.설치 확인
```
python --version
```
3.필요한 패키지 설치
터미널에서 아래 명령어 실행:
```
pip install -r requirements.txt
```
4️⃣ 프로젝트 빌드 및 실행
Gradle로 프로젝트 빌드
```
./gradlew build
```
SpringBoot 애플리케이션 실행

빌드 후 생성된 .jar 파일 실행
```
java -jar build/libs/<생성된_jar파일명>.jar
```
페이지가 뜨면 성공 🎉




끝! 😊😊😊😊😊