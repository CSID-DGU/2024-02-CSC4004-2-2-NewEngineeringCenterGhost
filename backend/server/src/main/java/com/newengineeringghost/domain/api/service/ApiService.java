package com.newengineeringghost.domain.api.service;

import com.newengineeringghost.domain.api.dto.ModelDataDto;
import com.newengineeringghost.domain.api.dto.PrecisionMeasurementDto;
import com.newengineeringghost.domain.api.dto.ResponseDataDto;
import com.newengineeringghost.domain.api.entity.ResponseData;
import com.newengineeringghost.domain.api.repository.ResponseDataRepository;
import jakarta.annotation.PreDestroy;
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

    private Process pythonServerProcess;

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
//    // SpringApplication 실행 시 자동으로 python server 실행
//    public void startPythonServer(String argument) throws IOException {
//        // Todo : 파일경로 ${Path} 등으로 변경
//        ProcessBuilder processBuilder = new ProcessBuilder("python3", "/home/testuser/project/backend/server/src/main/resources/core/server.py", argument);
//        pythonServerProcess = processBuilder.start();
//        logger.info("Process: {}", pythonServerProcess);
//    }
//
//    // SpringApplication 종료 시 자동으로 python server 종료 -> 메모리 누수 막기 위함
//    @PreDestroy
//    public void stopPythonServer() {
//        if (pythonServerProcess != null && pythonServerProcess.isAlive()) {
//            pythonServerProcess.destroy();
//            logger.info("Python process with PID {} has been destroyed.", pythonServerProcess.pid());
//        } else {
//            logger.info("Python Server Process is Null!");
//        }
//    }

    // 빠른 측정
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

    // python 모델이 반환하는 값 분리하는 함수
    private ModelDataDto parseResult(String result) {
        // 문자열의 앞뒤 괄호 제거 및 공백 제거
        String trimmedResult = result.substring(1, result.length() - 1).trim();
        logger.info("trimmedResult: {}", trimmedResult);

        // 첫 번째 콤마(,) 위치 찾기
        int firstCommaIndex = trimmedResult.indexOf(",");
        logger.info("firstCommaIndex: {}", firstCommaIndex);

        // 실수형 확률 추출
        double probability = Double.parseDouble(trimmedResult.substring(0, firstCommaIndex).trim());
        logger.info("probability: {}", probability);

        // 문자열 리스트 추출 (리스트는 대괄호로 둘러싸여 있음)
        String messagesString = trimmedResult.substring(firstCommaIndex + 1).trim();
        logger.info("messagesString: {}", messagesString);
        messagesString = messagesString.substring(1, messagesString.length() - 1); // 대괄호 제거
        logger.info("messagesString: {}", messagesString);

        // DTO 생성 및 반환
        return new ModelDataDto(probability, messagesString);
    }

    // 정밀 측정
    public Object precisionMeasurement(String url) throws IOException {
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

        // 실행 결과 파싱
        String result = resultBuilder.toString().trim();
        ModelDataDto modelDataDto = parseResult(result);

        logger.info("Dto.probability: {}", modelDataDto.getProbability());
        logger.info("Dto.sentence: {}", modelDataDto.getSentence());

        // 확률 값에 따라 반환
        if (modelDataDto.getProbability() > 0.5) {
            // Todo : mongodb 저장
            return new PrecisionMeasurementDto(modelDataDto.getProbability(), modelDataDto.getSentence(), openAI(modelDataDto.getSentence()));
        } else {
            // Todo : mongodb 저장
            return modelDataDto.getProbability();
        }
    }

    public String openAI(String sentence) throws IOException {
        // Todo : 파일경로 ${Path} 등으로 변경
        ProcessBuilder processBuilder = new ProcessBuilder("python3", "/home/testuser/project/backend/server/src/main/resources/openai/TestOpenAi.py", sentence);
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

        logger.info("openAI resultBuilder: {}", resultBuilder);

        // 실행 결과 파싱
        return resultBuilder.toString().trim();
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

    // 사용자 정의 측정
    public Object customMeasurement(String url) throws IOException{
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

        // 실행 결과 파싱
        String result = resultBuilder.toString().trim();
        ModelDataDto modelDataDto = parseResult(result);

        logger.info("Dto.probability: {}", modelDataDto.getProbability());
        logger.info("Dto.sentence: {}", modelDataDto.getSentence());

        // 확률 값에 따라 반환
        if (modelDataDto.getProbability() > 0.5) {
            // Todo : mongodb 저장
            return new PrecisionMeasurementDto(modelDataDto.getProbability(), modelDataDto.getSentence(), openAI(modelDataDto.getSentence()));
        } else {
            // Todo : mongodb 저장
            return modelDataDto.getProbability();
        }
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
