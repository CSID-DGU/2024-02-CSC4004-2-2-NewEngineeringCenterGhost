package com.newengineeringghost.domain.api.service;

import com.newengineeringghost.domain.api.dto.ResponseDataDto;
import com.newengineeringghost.domain.api.entity.ResponseData;
import com.newengineeringghost.domain.api.repository.ResponseDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ApiService {

    private ResponseDataRepository responseDataRepository;

    @Autowired
    public void setResponseDataRepository(ResponseDataRepository responseDataRepository) {this.responseDataRepository = responseDataRepository;}

    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    /*
    Todo : ServerApplication 실행 시에 자동으로 server.py 파일 실행되게 하는 코드 작성

    3가지 방법 중 하나 사용
    CommandLineRunner
    ApplicationRunner
    @EventListener(ApplicationReadyEvent.class)

    ServerApplication 시작과 동시에 실행
    ServerApplication 종료와 동시에 강제종료 -> 메모리 누수 막기 위함

    아래 코드 활용
     */
//    public double quickMeasurement(String url) throws IOException {
//        String content = webScraping(url);
//
//        // Todo : 파일경로 ${Path} 등으로 변경
//        ProcessBuilder processBuilder = new ProcessBuilder("python3", "/home/testuser/project/backend/server/src/main/resources/core/request.py", content);
//        Process process = processBuilder.start();
//        logger.info("Process: {}", process);
//
//        // 실행 결과 가져오기
//        InputStream inputStream = process.getInputStream();
//        logger.info("InputStream: {}", inputStream);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        logger.info("BufferedReader: {}", reader);
//
//        // 실행결과 저장
//        StringBuilder resultBuilder = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            resultBuilder.append(line).append(System.lineSeparator());
//        }
//
//        logger.info("Result: {}", resultBuilder);
//
//        String[] parts = resultBuilder.toString().split("[(),]");
//
//        String doubleString = parts[1].trim();
//
//        double probability = Double.parseDouble(doubleString);
//        logger.info("Probability: {}", probability);
//
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
//
//        return probability;
//    }

    // 빠른 정렬
    public double quickMeasurement(String url) throws IOException {
        String content = webScraping(url);

        // Todo : 파일경로 ${Path} 등으로 변경
        ProcessBuilder processBuilder = new ProcessBuilder("python3", "/home/testuser/project/backend/server/src/main/resources/core/request.py", content);
        Process process = processBuilder.start();
        logger.info("Process: {}", process);

        // 실행 결과 가져오기
        InputStream inputStream = process.getInputStream();
        logger.info("InputStream: {}", inputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        logger.info("BufferedReader: {}", reader);

        // 실행결과 저장
        StringBuilder resultBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            resultBuilder.append(line).append(System.lineSeparator());
        }

        logger.info("Result: {}", resultBuilder);

        String[] parts = resultBuilder.toString().split("[(),]");

        String doubleString = parts[1].trim();

        double probability = Double.parseDouble(doubleString);
        logger.info("Probability: {}", probability);

        return probability;
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

    반환형에 대한 고민)
    String
    WebContentsDto

    mongodb에 값 저장
     */
    public double precisionMeasurement(String url) throws IOException {
        String content = webScraping(url);

        // Todo : 파일경로 ${Path} 등으로 변경
        ProcessBuilder processBuilder = new ProcessBuilder("python3", "/home/testuser/project/backend/server/src/main/resources/core/request.py", content);
        Process process = processBuilder.start();
        logger.info("Process: {}", process);

        // 실행 결과 가져오기
        InputStream inputStream = process.getInputStream();
        logger.info("InputStream: {}", inputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        logger.info("BufferedReader: {}", reader);

        // 실행결과 저장
        StringBuilder resultBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            resultBuilder.append(line).append(System.lineSeparator());
        }

        logger.info("Result: {}", resultBuilder);

        String[] parts = resultBuilder.toString().split("[(),]");

        String doubleString = parts[1].trim();

        double probability = Double.parseDouble(doubleString);
        logger.info("Probability: {}", probability);

        /*
        Todo
        python 파일 실행결과 받아와서 Response data dto에 저장한 후,
        확률값이 50%를 넘냐 마냐에 따라 dto 값 중 전체 혹은 확률 값만 반환
         */

        return probability;
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

    반환형에 대한 고민)
    String
    WebContentsDto

    mongodb에 값 저장
     */
    public double customMeasurement(String url) throws IOException{
        String content = webScraping(url);

        // Todo : 파일경로 ${Path} 등으로 변경
        ProcessBuilder processBuilder = new ProcessBuilder("python3", "/home/testuser/project/backend/server/src/main/resources/core/request.py", content);
        Process process = processBuilder.start();
        logger.info("Process: {}", process);

        // 실행 결과 가져오기
        InputStream inputStream = process.getInputStream();
        logger.info("InputStream: {}", inputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        logger.info("BufferedReader: {}", reader);

        // 실행결과 저장
        StringBuilder resultBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            resultBuilder.append(line).append(System.lineSeparator());
        }

        logger.info("Result: {}", resultBuilder);

        String[] parts = resultBuilder.toString().split("[(),]");

        String doubleString = parts[1].trim();

        double probability = Double.parseDouble(doubleString);
        logger.info("Probability: {}", probability);

        /*
        Todo
        python 파일 실행결과 받아와서 Response data dto에 저장한 후,
        확률값이 50%를 넘냐 마냐에 따라 dto 값 중 전체 혹은 확률 값만 반환
         */

        return probability;
    }

    // Selenium 사용을 위해 ChromeDriver 설정
    public WebDriver getChromeDriver() {
        logger.info("Chrome Driver Start!");
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--lang=ko");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        return driver;
    }

    // 웹 페이지에서 제목 추출
    public String webScrapingTitle(String url) throws IOException {
        WebDriver driver = getChromeDriver();
        logger.info("Chrome Driver Info: {}", driver);

        if (!ObjectUtils.isEmpty(driver)) {
            driver.get(url);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(100)); // page 전체가 넘어올 때까지 대기(5초)
            logger.info("Chrome Driver Info: {}", driver);

            // 제목 추출
            String title = driver.getTitle();
            logger.info("WebPage Title: {}", title);

            driver.quit();

            return title;
        } else {
            return "No Element";
        }
    }

    // 웹 페이지에서 본문 추출
    public String webScrapingContent(String url) throws IOException {
        WebDriver driver = getChromeDriver();
        logger.info("Chrome Driver Info: {}", driver);

        if (!ObjectUtils.isEmpty(driver)) {
            driver.get(url);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(100)); // page 전체가 넘어올 때까지 대기(5초)
            logger.info("Chrome Driver Info: {}", driver);

            // 본문 내용 추출
            String content;
            try {
                // 시도 1: <article> 요소 찾기
                WebElement webElement2 = driver.findElement(By.tagName("article"));
                content = webElement2.getText();
                logger.info("WebPage Content from <article>: {}", content);
            } catch (NoSuchElementException e) {
                // 시도 2: <article> 요소가 없을 경우 모든 <span> 요소 찾기
                List<WebElement> spanElements = driver.findElements(By.tagName("span"));
                StringBuilder contentBuilder = new StringBuilder();
                for (WebElement span : spanElements) {
                    contentBuilder.append(span.getText()).append("\n");
                }
                content = contentBuilder.toString();
                logger.info("WebPage Content from all <span>: {}", content);
            }

            driver.quit();

            return content;
        } else {
            return "No Element";
        }
    }

    // 웹 페이지에서 제목&본문 추출
    public String webScraping(String url) throws IOException {
        WebDriver driver = getChromeDriver();
        logger.info("Chrome Driver Info: {}", driver);

        if (!ObjectUtils.isEmpty(driver)) {
            driver.get(url);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(100)); // page 전체가 넘어올 때까지 대기(5초)
            logger.info("Chrome Driver Info: {}", driver);

            // 제목 추출
            String title = driver.getTitle();
            logger.info("WebPage Title: {}", title);

            // 본문 내용 추출
            String content;
            try {
                // 시도 1: <article> 요소 찾기
                WebElement webElement2 = driver.findElement(By.tagName("article"));
                content = webElement2.getText();
                logger.info("WebPage Content from <article>: {}", content);
            } catch (NoSuchElementException e) {
                // 시도 2: <article> 요소가 없을 경우 모든 <span> 요소 찾기
                List<WebElement> spanElements = driver.findElements(By.tagName("span"));
                StringBuilder contentBuilder = new StringBuilder();
                for (WebElement span : spanElements) {
                    contentBuilder.append(span.getText()).append("\n");
                }
                content = contentBuilder.toString();
                logger.info("WebPage Content from all <span>: {}", content);
            }

            StringBuilder result = new StringBuilder();
            result.append(title);
            result.append(".");
            result.append(content);
            logger.info("WEB SCRAPING RESULT: {}", result);

            driver.quit();

            return result.toString();
        } else {
            return "";
        }
    }

    // Todo : OCR 위한 이미지 다운로드 기능, 사용자 정의 측정에서 사용
    // 웹 페이지에서 이미지 추출
    public List<String> webScrapingImage(String url) throws IOException {
        WebDriver driver = getChromeDriver();
        logger.info("Chrome Driver Info: {}", driver);

        if (!ObjectUtils.isEmpty(driver)) {
            driver.get(url);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(100)); // page 전체가 넘어올 때까지 대기(5초)
            logger.info("Chrome Driver Info: {}", driver);

            // 이미지 추출
            WebElement articleElement = driver.findElement(By.tagName("article"));
            List<WebElement> imageElements = articleElement.findElements(By.tagName("img"));

            // 모든 이미지 src를 추출하여 리스트에 저장
            List<String> imgSrcs = new ArrayList<>();
            for (WebElement imageElement : imageElements) {
                String img = imageElement.getAttribute("src");
                imgSrcs.add(img);
                logger.info("Image: {}", img);
            }

            driver.quit();

            // 리스트를 문자열로 반환 (필요에 따라 적절히 수정 가능)
            return imgSrcs;
        } else {
            List<String> imgSrcs = new ArrayList<>();
            String img = "no element";
            imgSrcs.add(img);
            logger.info("Image: {}", imgSrcs);

            return imgSrcs;
        }
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