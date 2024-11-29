# 2024-02-CSC4004-2-2-NewEngineeringCenterGhost
👻CSC4004-공개SW프로젝트 2조 신공학관 지박령입니다👻

팀장 : 정재환<br>
팀원 : 신동주, 정태호, 조성원

## 팀원 소개
| 이름 | 학번 | 역할 | GITHUB |
| :---: | :---: | :---: | :---: |
| 정재환 | 2020112023 | 팀장, 백엔드, 서버 | [@smturtle2](https://github.com/smturtle2) |
| 신동주 | 2020112541 | AI | [@hwnnn](https://github.com/hwnnn) |
| 정태호 | 2022113556 | 크롬 확장 프로그램, 프론트엔드 | [@Taehoo-Jeong](https://github.com/Taehoo-Jeong) |
| 조성원 | 2020112037 | AI | [@sungwon01](https://github.com/sungwon01) |

## 프로젝트 소개
- 인터넷의 발달로 접할 수 있는 정보의 양이 많아지면서 해당 정보의 진위여부가 중요하게 되었다. 하지만 한 사람이 접하는 모든 정보의 진위를 파악하는데는 무리가 있다. 본 프로젝트는 이러한 상황 속에서 사용자에게 해당 정보가 낚시성 정보인지 파악하는데 도움을 준다.

## 프로젝트 파일 구조
```
.idea
 ┣ .gitignore
 ┣ misc.xml
 ┣ modules.xml
 ┣ openswproject.iml
 ┗ vcs.xml
```

```
backend
 ┣ modules
 ┃ ┗ ocr
 ┃ ┃ ┗ TempImage
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
    - 기사의 링크 위에 마우스 커서를 올려두면 해당 기사가 낚시성 정보인지 판단한다.

  2. 정밀 측정 기능
    - 측정하고 싶은 기사 페이지에 들어가 측정을 시작하면 해당 페이지의 정보들에 기반하여 낚시성 정보인지 판단한다.

  3. 사용자 정의 측정 기능
    - 사용자가 낚시성 정보인지 판단하고 싶은 구문들을 따로 모은 후 사용자가 측정을 시작하면 해당 구문들이 낚시성 정보인지 판단한다.

- 나아가 블로그, 카드뉴스 형식의 SNS 게시물에도 이를 적용 시킬 수 있다.

## 스킬 스택
- OS : 
<img src="https://img.shields.io/badge/Linux-FCC624?style=flat-square&logo=Linux&logoColor=white">
<img src="https://img.shields.io/badge/Ubuntu-E95420?style=flat-square&logo=Ubuntu&logoColor=white">

- IDE : 
<img src="https://img.shields.io/badge/Visual Studio Code-007ACC?style=flat-square&logo=Visual Studio Code&logoColor=white">

- Server :
<img src="https://img.shields.io/badge/MongoDB-47A248?style=flat-square&logo=MongoDB&logoColor=white">
<img src="https://img.shields.io/badge/Selenium-43B02A?style=flat-square&logo=Selenium&logoColor=white">

- Backend : 
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white">
<img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white">

- Frontend : 
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=JavaScript&logoColor=white">
<img src="https://img.shields.io/badge/JSON-000000?style=flat-square&logo=JSON&logoColor=white">
<img src="https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=HTML5&logoColor=white">
<img src="https://img.shields.io/badge/CSS3-1572B6?style=flat-square&logo=CSS3&logoColor=white">

- AI : 
<img src="https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=Python&logoColor=white">
<img src="https://img.shields.io/badge/PyTorch-EE4C2C?style=flat-square&logo=PyTorch&logoColor=white">
<img src="https://img.shields.io/badge/OpenAI-412991?style=flat-square&logo=OpenAI&logoColor=white">
<img src="https://img.shields.io/badge/OpenCV-5C3EE8?style=flat-square&logo=OpenCV&logoColor=white">

## 시연 영상
- 링크 : 

## 배포 URL
- 배포 URL : 