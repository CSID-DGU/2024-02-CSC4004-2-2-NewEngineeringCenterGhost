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
- 본 프로젝트는 인터넷상의 낚시성 정보로 인한 사용자 혼란과 시간 낭비 문제를 해결하기 위해 AI 기반 크롬 확장 프로그램을 개발하는 것을 목표로 한다. 주요 기능으로는 웹페이지 제목과 본문 간의 일치 여부, 본문 내 정보의 일관성을 실시간으로 분석하여 신뢰도를 점수로 제공하며, 대형 언어 모델(LLM)을 활용해 낚시성 정보로 판단된 근거를 해설하는 기능이 포함된다. 사용자는 링크에 마우스를 올리거나 웹페이지에 접속하면 낚시성 정보의 확률과 판단 근거를 확인할 수 있으며, 임의의 텍스트나 이미지를 선택해 정밀 분석도 가능하다.

이 확장 프로그램은 낚시성 정보 탐지뿐만 아니라, 사용자가 신뢰도 낮은 콘텐츠를 사전에 구별하고 클릭 여부를 판단할 수 있도록 돕는다. AI 모델은 [AIHub]의 방대한 데이터를 학습하여 신뢰성 높은 판별 결과를 제공하며, 페이지 내용 중 낚시성으로 판단된 문장을 강조 표시한다. 또한, LLM 기반 해설 기능은 단순한 판별 결과를 넘어, 낚시성 정보로 간주된 이유를 사용자에게 명확히 설명함으로써 정보 이해도를 높인다.

이를 통해 사용자는 불필요한 클릭을 줄이고 원하는 정보를 효율적으로 탐색할 수 있다. 궁극적으로, 본 프로젝트는 인터넷 정보 소비의 질을 향상시키고, 신뢰성 높은 정보 소비 문화를 확산시킬 수 있는 도구로 기능할 것이다. 더 나아가 언론사, 기업, 블로그, 소셜 미디어 등 다양한 분야에서 정보 신뢰성 분석과 개선에 활용될 가능성을 가진다.

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
    - 기사의 링크 위에 마우스 커서를 올려두면 서버로 해당 링크를 전송하여 서버에서 정보를 분석한다.

    - 분석을 바탕으로 해당 기사가 낚시성 정보인지 판단하며 낚시성 포함 확률을 링크 바로위에 표시한다.


  2. 정밀 측정 기능
    - 측정하고 싶은 기사 페이지에 들어가 마우스 우클릭을 하여 컨텍스트 메뉴에서 '측정하기' 버튼을 누른다. 

    - 다음으로 서버로 해당 페이지의 데이터를 전송하며 분석 후, 서버로부터 받은 정보를 기반으로 상단 배너에 낚시성 정보 확률과 LLM을 통해 생성된 해설을 같이 제공한다.

    - 낚시성 정보인 확률이 50% 이상인 경우에는 배너의 색상이 빨간색으로 처리되고 50% 미만인 경우에는 초록색으로 나타난다.


  3. 사용자 정의 측정 기능
    - 사용자가 낚시성 정보인지 판단하고 싶은 텍스트은 드래그를 한 후에 우클릭을 통해 컨텍스트 메뉴에서 '측정 정보 추가'를 하여 데이터를 임시 저장한다.

    - 사용자가 낚시성 정보인지 판단하고 싶은 이미지는 마우스 우클릭을 통해 컨텍스트 메뉴에서 '측정 정보 추가'를 하여 데이터를 임시 저장한다.

    - 다음으로 마우스 우클릭 후 컨텍스트 메뉴에서 '측정하기' 버튼을 클릭하면 서버로 데이터를 전송하여 분석을 하며 분석 결과와 배너의 모습은 정밀 측정과 동일하다.

    - 추가로 낚시성 정보인 확률이 높은 텍스트는 빨갛게 하이라이트 처리가 되며, 낚시성 정보인 확률이 높은 이미지는 테두리가 빨갛게 강조되고 경고 이미지가 뜬다.

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