package com.newengineeringghost.domain.api.service;

import com.newengineeringghost.domain.api.dto.ResponseDataDto;
import com.newengineeringghost.domain.api.dto.WebContentsDto;
import com.newengineeringghost.domain.api.entity.ResponseData;
import com.newengineeringghost.domain.api.repository.ResponseDataRepository;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class ApiService {

    private ResponseDataRepository responseDataRepository;

    @Autowired
    public void setResponseDataRepository(ResponseDataRepository responseDataRepository) {this.responseDataRepository = responseDataRepository;}

    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
    /*
    log 사용 예시
    logger.info("Html Content: {}", document);
    logger.error("Html Content: {}", document);
     */

    /*
    Todo: 빠른 측정

    기능)
    사용자가 링크 위에 마우스를 올리면 서버로 해당 링크 전송
    서버에서 링크의 정보를 분석한 후, 낚시성 정보 포함 확률을 위에 바로 표시

    request : 측정할 페이지 링크
    response : 확률값
     */
    public String quickMeasurement(String url) throws IOException {
        // Todo : 해당 url에서 text 추출

        // Todo : text를 매개변수로 하여 Python 파일에 전달 및 실행

        // Todo : 파일 경로 변경 - python 파일들을 resource/static 안에 넣고 동작하는지 확인하기
        ProcessBuilder processBuilder = new ProcessBuilder("python3", "/Users/jaehwan/Desktop/JaeHwan/WorkSpace/python/test/helloworld.py", url);
        Process process = processBuilder.start();

        // 실행 결과 가져오기
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // Todo : 실행결과 출력에서 실행결과 저장으로 변경
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // Todo : python 파일 실행결과 중 probability만 return

//        // 오류 메세지 출력
//        InputStream errorStream = process.getErrorStream();
//        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
//
//        String errorLine;
//        while ((errorLine = errorReader.readLine()) != null) {
//            System.out.println(errorLine);
//        }
//
//        // 프로세스 종료 대기
//        int waitCode = process.waitFor();
//        System.out.println(waitCode);
//
//        // 프로세스 강제 종료
//        process.destroy();
//
//        // 종료 코드 확인
//        int exitCode = process.exitValue();
//        System.out.println(exitCode);

        return "33.3%";
    }

    /*
    Todo : 정밀 측정

    기능)
    사용자가 링크에 접속한 후, 우클릭하면 측정하기 버튼이 나타난다
    측정하기 버튼을 누르면, 서버로 해당 페이지의 데이터를 전송한다
    서버에서 받은 정볼르 바탕으로 페이지 상단에 낚시성 정보 확률을 표시한다
    확률이 50% 이상일 경우, LLM을 통해 생성된 해설을 상단에 제공하고, 낚시성 정보로 판단된 텍스트나 이미지를 강조한다.

    request : 측정할 페이지 링크
    response : 확률값, (50%이상일 경우-> 해설, 문장 위치, 문장 길이까지)
     */
    public String precisionMeasurement(String url) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("python3", "test.py", url);
        Process process = processBuilder.start();

        /*
        직접 경로 설정
        ProcessBuilder("C:/python/python311/bin..", (생략))
        경로 예시 - mac
        /Users/jaehwan/Desktop/JaeHwan/WorkSpace/python/test/helloworld.py
         */

        // 실행 결과 가져오기
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        /*
        Todo
        python 파일 실행결과 받아와서 Response data dto에 저장한 후,
        확률값이 50%를 넘냐 마냐에 따라 dto 값 중 전체 혹은 확률 값만 반환
         */

//        // 오류 메세지 출력
//        InputStream errorStream = process.getErrorStream();
//        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
//
//        String errorLine;
//        while ((errorLine = errorReader.readLine()) != null) {
//            System.out.println(errorLine);
//        }
//
//        // 프로세스 종료 대기
//        int waitCode = process.waitFor();
//        System.out.println(waitCode);
//
//        // 프로세스 강제 종료
//        process.destroy();
//
//        // 종료 코드 확인
//        int exitCode = process.exitValue();
//        System.out.println(exitCode);

        return "";
    }

    /*
    Todo : 사용자 정의 측정

    기능)
    사용자가 임의의 이미지나 드래그한 텍스트를 우클릭하면, '측정 정보 추가' 버튼이 나타남
    이를 누르면 해당 데이터나 요소가 임시 저장됨
    저장된 데이터가 있는 상태에서 우클릭하면 '측정하기' 버튼이 나타나며, 이 버튼을 통해 서버로 데이터를 전송하여 분석을 진행
    분석 결과는 정밀 측정과 동일한 방법으로 처리

    request : 페이지 링크? / 혹은 해당 이미지나 텍스트?
    response : 확률값, (50%이상일 경우-> 해설, 문장 위치, 문장 길이까지)
     */
    public String customMeasurement(String url) throws IOException{
        ProcessBuilder processBuilder = new ProcessBuilder("python3", "test.py", url);
        Process process = processBuilder.start();

        /*
        직접 경로 설정
        ProcessBuilder("C:/python/python311/bin..", (생략))
        경로 예시 - mac
        /Users/jaehwan/Desktop/JaeHwan/WorkSpace/python/test/helloworld.py
         */

        // 실행 결과 가져오기
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        /*
        Todo
        python 파일 실행결과 받아와서 Response data dto에 저장한 후,
        확률값이 50%를 넘냐 마냐에 따라 dto 값 중 전체 혹은 확률 값만 반환
         */

//        // 오류 메세지 출력
//        InputStream errorStream = process.getErrorStream();
//        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
//
//        String errorLine;
//        while ((errorLine = errorReader.readLine()) != null) {
//            System.out.println(errorLine);
//        }
//
//        // 프로세스 종료 대기
//        int waitCode = process.waitFor();
//        System.out.println(waitCode);
//
//        // 프로세스 강제 종료
//        process.destroy();
//
//        // 종료 코드 확인
//        int exitCode = process.exitValue();
//        System.out.println(exitCode);

        return "";
    }

    /*
    Todo : OCR 위한 이미지 다운로드 기능, 사용자 정의 측정에서 사용
    image를 server단에 저장
    이후에 이미지 파일을 python에 같이 넘기기?
    이미지 파일을 다운받고, python에 텍스트와 함께 넘기면서 이미지 파일은 삭제

    동적 웹 페이지와 정적 웹 페이지 구분
     */
    public String downlaodImage(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements imgs = doc.select("img");

        System.out.println(imgs);
        return "";
    }

    public WebDriver getChromeDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--lang=ko");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.setCapability("ignoreProtectedModeSettins", true);

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        return driver;
    }

    public WebContentsDto webCrawling(String url) throws IOException {
        WebDriver driver = getChromeDriver();

        driver.get(url);

        WebElement title = driver.findElement(By.tagName("title"));
        WebElement content = driver.findElement(By.className("_article_content"));

        driver.quit();

        logger.info("title: {}", title);
        logger.info("content: {}", content);

        WebContentsDto webContentsDto = new WebContentsDto(title.toString(), content.toString());
        logger.info("webContentsDto: {}", webContentsDto);

        return webContentsDto;
    }

//    public WebContentsDto webCrawling(String url) throws IOException {
//        // 연결은 잘 됨
//        Document document = Jsoup.connect(url).get();
//
//        logger.info("document content: {}", document.toString());
//
//        String title = extractTitle(document);
//        String content = extractContect(document);
//
//        logger.info("title: {}", title);
//        logger.info("content: {}", content);
//
//        WebContentsDto webContentsDto = new WebContentsDto(title, content);
//        logger.info("webContentsDto: {}", webContentsDto);
//
//        return webContentsDto;
//    }

    private String extractTitle(Document document) {
        Elements titleElement = document.select("title");
        return titleElement.isEmpty() ? titleElement.text() : "Title not found.";
    }

    private String extractContect(Document document) {
        Elements contectElement = document.select(".article_p");
        return contectElement.isEmpty() ? contectElement.text() : "Content not found.";
    }

    // Todo : 제목과 본문 추출
    public String downloadText(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements text = doc.select("article");

        System.out.println(text);

        return "";
    }

    /*
    Todo : 링크 분석 및 데이터 처리

    기능)
    사용자가 전송한 링크가 확장 프로그램에서 지원하는 사이트인지 확인
    지원되는 사이트인 경우, BeautifulSoup을 사용해 링크 내 데이터 추출
    추출된 데이터가 유효하다면, 해당 데이터를 '낚시성 정보 판별 기능'을 통해 분석하고,
    결과로 나온 확률값을 사용자에게 전송

    링크를 parsing하여 domain별로 분류?
    news인지
    기사인지
    blog인지
    instagram인지

    switch문 사용
    처음이 news
    그 다음이 기사
    그 다음이 인스타
    나머지를 블로그 처리?

    request :
    response :
     */
    public void classifyLink(String url) {

    }

    // mongodb 연동 test
    public ResponseDataDto getData(String link) {
        ResponseData responseData = responseDataRepository.findResponseDataByLink(link);

        ResponseDataDto responseDto = new ResponseDataDto(
                responseData.getLink(),
                responseData.getProbability(),
                responseData.getSentencePosition(),
                responseData.getSentenceLength(),
                responseData.getExplanation());

        return responseDto;
    }

    // mongodb 연동 test
    public String postData(ResponseDataDto responseDto) {

        ResponseData responseData = new ResponseData(
                responseDto.getLink(),
                responseDto.getProbability(),
                responseDto.getSentencePosition(),
                responseDto.getSentenceLength(),
                responseDto.getExplanation()
        );

        responseDataRepository.save(responseData);

        return "success";
    }

}
