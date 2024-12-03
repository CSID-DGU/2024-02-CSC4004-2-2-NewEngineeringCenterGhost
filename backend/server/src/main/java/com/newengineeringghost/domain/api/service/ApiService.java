package com.newengineeringghost.domain.api.service;

import com.newengineeringghost.domain.api.dto.ModelDataDto;
import com.newengineeringghost.domain.api.dto.PrecisionMeasurementDto;
import com.newengineeringghost.domain.api.dto.WebScrappingResultDto;
import com.newengineeringghost.domain.api.entity.ResponseData;
import com.newengineeringghost.domain.api.repository.ResponseDataRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.*;

@Slf4j
@Service
public class ApiService {

    private static final Map<String, String> urlToXpathMap = new HashMap<>();

    static {
        // 기사
        urlToXpathMap.put("news.kbs.co.kr","//*[@id=\"cont_newstext\"]"); // KBS 뉴스
        urlToXpathMap.put("www.hankyung.com","//*[@id=\"articletxt\"]"); //한국경제
        urlToXpathMap.put("imnews.imbc.com","//*[@id=\"content\"]/div/section[1]/article/div[2]/div[4]"); // MBC 뉴스
        urlToXpathMap.put("www.ohmynews.com","//*[@id=\"content_wrap\"]/div[1]/div[3]/div[1]/div[1]/div[1]/div"); // 오마이뉴스
        urlToXpathMap.put("www.mk.co.kr","//*[@id=\"container\"]/section/div[3]/section/div[1]/div[1]/div[1]"); // 매일경제
        urlToXpathMap.put("www.dailian.co.kr","//*[@id=\"contentsArea\"]/div[1]/div[4]/div[2]/div[2]/description"); // 데일리안
        urlToXpathMap.put("www.nocutnews.co.kr","//*[@id=\"pnlContent\"]"); // 노컷뉴스
        urlToXpathMap.put("view.asiae.co.kr","//*[@id=\"txt_area\"]"); // 아시아경제
        urlToXpathMap.put("www.edaily.co.kr","//*[@id=\"contents\"]/section[1]/section[1]/div[1]/div[3]/div[1]"); // 이데일리
        urlToXpathMap.put("biz.heraldcorp.com","//*[@id=\"articleText\"]"); // 해럴드경제
        urlToXpathMap.put("zdnet.co.kr","//*[@id=\"articleBody\"]");
        urlToXpathMap.put("www.seoul.co.kr","//*[@id=\"articleContent\"]/div"); // 서울신문
        urlToXpathMap.put("www.osen.co.kr","//*[@id=\"articleBody\"]"); // OSEN
        urlToXpathMap.put("news.sbs.co.kr","//*[@id=\"container\"]/div[1]/div[3]/div[2]/div[1]/div[1]/div[2]"); // SBS 뉴스
        urlToXpathMap.put("newstapa.org","//*[@id=\"editor_fontsize\"]"); //뉴스타파
        urlToXpathMap.put("www.hankookilbo.com","/html/body/div[2]/div/div[4]/div/div[1]"); // 한국일보
        urlToXpathMap.put("isplus.com","//*[@id=\"article_body\"]"); // 일간스포츠
        urlToXpathMap.put("www.newsis.com","//*[@id=\"content\"]/div[1]/div[1]/div[3]/article"); // 뉴시스
        urlToXpathMap.put("www.inews24.com","//*[@id=\"articleBody\"]"); // 아이뉴스
        urlToXpathMap.put("mydaily.co.kr","//*[@id=\"container\"]/div[1]/div[4]/div[1]/div"); // 마이데일리
        urlToXpathMap.put("www.donga.com","//*[@id=\"contents\"]/div[2]/div/div[1]/section[1]"); // 동아일보
        urlToXpathMap.put("news.jtbc.co.kr","//*[@id=\"wrapper\"]/div/div[6]/div/div/div/div/div[1]/div[1]"); // JTBC 뉴스
        urlToXpathMap.put("www.ytn.co.kr","//*[@id=\"CmAdContent\"]/span"); // ytn 뉴스
        urlToXpathMap.put("www.newdaily.co.kr","//*[@id=\"article_conent\"]"); // 뉴데일리
        urlToXpathMap.put("www.dt.co.kr","//*[@id=\"v-left-scroll-in\"]/div[2]/div/div[2]"); // 디지털타임스
        urlToXpathMap.put("sports.chosun.com","//*[@id=\"articleBody\"]/div/div/font"); // 스포츠조선
        urlToXpathMap.put("www.chosun.com","//*[@id=\"fusion-app\"]/div[1]/div[2]/div/section/article/section"); // 조선일보
        urlToXpathMap.put("www.sportsseoul.com","//*[@id=\"article-body\"]"); // 스포츠서울
        urlToXpathMap.put("www.khan.co.kr","//*[@id=\"articleBody\"]"); // 경향신문
        urlToXpathMap.put("sports.donga.com","//*[@id=\"article_body\"]"); // 스포츠동아
        urlToXpathMap.put("news.mt.co.kr","//*[@id=\"textBody\"]"); // 머니투데이
        urlToXpathMap.put("www.etnews.com","//*[@id=\"articleBody\"]/p"); // 전자신문
        urlToXpathMap.put("www.sedaily.com","//*[@id=\"v-left-scroll-in\"]/div[2]/div[1]/div[2]"); // 서울경제
        urlToXpathMap.put("www.joongang.co.kr","//*[@id=\"article_body\"]"); // 중앙일보
        urlToXpathMap.put("www.sisain.co.kr","//*[@id=\"article-view-content-div\"]"); // 시사인
        urlToXpathMap.put("www.wowtv.co.kr","//*[@id=\"divNewsContent\"]"); // 한국경제TV
        urlToXpathMap.put("www.yonhapnewstv.co.kr","//*[@id=\"articleBody\"]"); // 연합뉴스
        urlToXpathMap.put("www.kmib.co.kr","//*[@id=\"articleBody\"]"); // 국민일보
        urlToXpathMap.put("www.mbn.co.kr","//*[@id=\"newsViewArea\"]"); // MBN 뉴스
        urlToXpathMap.put("biz.chosun.com","//*[@id=\"fusion-app\"]/div[1]/div[2]/div/section/article/section"); // 조선비즈
        urlToXpathMap.put("www.segye.com","//*[@id=\"wps_layout1_box2\"]"); // 세계일보
        urlToXpathMap.put("www.fnnews.com","//*[@id=\"article_content\"]"); // 파이낸셜 뉴스
        urlToXpathMap.put("sportalkorea.com","//*[@id=\"CmAdContent\"]/div[2]/div/div"); // 스포탈코리아
        urlToXpathMap.put("www.hani.co.kr","//*[@id=\"renewal2023\"]/span"); // 한겨레
        urlToXpathMap.put("n.news.naver.com","//*[@id=\"dic_area\"]"); // 네이버뉴스
    }

    private final ResponseDataRepository responseDataRepository;

    @Autowired
    public ApiService(ResponseDataRepository responseDataRepository) {
        this.responseDataRepository = responseDataRepository;
    }

    @Value("${python.script.path.request}")
    private String requestScriptPath;

    @Value("${python.script.path.openAi}")
    private String openAiScriptPath;

    @Value("${python.script.path.ocr}")
    private String ocrScriptPath;

    @Value("${chrome-driver.path}")
    private String chromeDriverScriptPath;

    private WebDriver driver;

    // Selenium 사용을 위해 ChromeDriver 설정 & 초기에 한번만 실행
    @PostConstruct
    public void getChromeDriver() {
        log.info("Chrome Driver Start!");
        System.setProperty("webdriver.chrome.driver", chromeDriverScriptPath);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--lang=ko");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        driver.get("https://www.instagram.com/");
        // 로그인 폼에서 사용자 이름 또는 이메일을 입력하는 필드 요소 찾기
        WebElement e = driver.findElement(By.xpath("//*[@id=\"loginForm\"]/div/div[1]/div/label/input"));
        // @test_account_for_oss라는 사용자 이름 입력
        e.sendKeys("@test_account_for_oss");
        // 비밀번호 입력 필드 요소 찾기
        e = driver.findElement(By.xpath("//*[@id=\"loginForm\"]/div/div[2]/div/label/input"));
        // oss_account라는 비밀번호 입력
        e.sendKeys("oss_account");
        // 로그인 버튼 요소 찾기
        e = driver.findElement(By.xpath("//*[@id=\"loginForm\"]/div/div[3]/button"));
        // 로그인 버튼 누름
        e.click();

        // 페이지 로드 전략 설정
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);

    }

    // ServerApplication 종료 시에 chrome driver quit
    @PreDestroy
    public void destroyChromeDriver() {
        if (!ObjectUtils.isEmpty(driver)) {
            driver.quit();
            log.info("Chrome Driver Quit!");
        } else {
            log.info("Chrome Driver is null!");
        }
    }

    // python 파일 실행 : 매개변수 2개
    public String pythonFileRun_2(String filePath, String content) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("python3", filePath, content);
        Process process = processBuilder.start();
        log.info("Process: {}", process);

        // 실행 결과 가져오기
        InputStream inputStream = process.getInputStream();
        log.info("InputStream: {}", inputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        log.info("BufferedReader: {}", reader);

        // 실행결과 저장
        StringBuilder resultBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            resultBuilder.append(line).append(System.lineSeparator());
        }
        log.info("Result: {}", resultBuilder);

        return resultBuilder.toString().trim();
    }

    // python 파일 실행 : 매개변수 3개(파일 경로, 제목, 본문)
    public String pythonFileRun_3(String filePath, String title, String content) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("python3", filePath, title, content);
        Process process = processBuilder.start();
        log.info("Process: {}", process);

        // 실행 결과 가져오기
        InputStream inputStream = process.getInputStream();
        log.info("InputStream: {}", inputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        log.info("BufferedReader: {}", reader);

        // 실행결과 저장
        StringBuilder resultBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            resultBuilder.append(line).append(System.lineSeparator());
        }
        log.info("Result: {}", resultBuilder);

        return resultBuilder.toString().trim();
    }

    // 빠른 측정
    public double quickMeasurement(String url) throws IOException {
        ResponseData responseData = responseDataRepository.findResponseDataByLink(url).orElse(null);

        // DB에 해당 URL에 대한 값이 있는 경우
        if (responseData != null) {
            return responseData.getProbability();

        }
        // DB에 해당 URL에 대한 값이 없는 경우
        else {
            WebScrappingResultDto webScrappingResultDto = webScraping(url);

            String result = pythonFileRun_3(requestScriptPath, webScrappingResultDto.getTitle(), webScrappingResultDto.getContent());

            String[] parts = result.split("[(),]");

            String doubleString = parts[1].trim();

            double probability = Double.parseDouble(doubleString);
            log.info("Probability: {}", probability);

            // mongodb에 값 저장
            ResponseData data = new ResponseData(
                    url,
                    probability,
                    null,
                    null
            );

            responseDataRepository.save(data);

            return probability;
        }
    }

    // 정밀 측정
    public Object precisionMeasurement(String url) throws IOException {
        ResponseData responseData = responseDataRepository.findResponseDataByLink(url).orElse(null);

        // DB에 해당 URL에 대한 값이 있는 경우
        if (responseData != null) {
            // DB에 해당 URL에 대한 모든 값이 있는 경우 (확률, 낚시성 의심 문장, 해설)
            if ((responseData.getFishingSentence() != null) && (responseData.getExplanation() != null)) {
                if (responseData.getProbability() > 0.5) {
                    return new PrecisionMeasurementDto(responseData.getProbability(), responseData.getFishingSentence(), responseData.getExplanation());
                }
                else {
                    return responseData.getProbability();
                }
            }
            // DB에 해당 URL에 대한 확률 값만 있는 경우 (낚시성 의심 문장, 해설은 null 값)
            else {
                WebScrappingResultDto webScrappingResultDto = webScraping(url);

                String result = pythonFileRun_3(requestScriptPath, webScrappingResultDto.getTitle(), webScrappingResultDto.getContent());
                ModelDataDto modelDataDto = parseResult(result);

                log.info("Dto.probability: {}", modelDataDto.getProbability());
                log.info("Dto.sentence: {}", modelDataDto.getSentence());

                String explanation = openAI(webScrappingResultDto.getContent(), modelDataDto.getSentence());
                log.info("Explanation: {}", explanation);

                // 기존에 저장된 값 update (null -> data)
                responseData.setFishingSentence(modelDataDto.getSentence());
                responseData.setExplanation(explanation);

                responseDataRepository.save(responseData);

                // 확률 값에 따라 반환
                if (modelDataDto.getProbability() > 0.5) {
                    return new PrecisionMeasurementDto(responseData.getProbability(), responseData.getFishingSentence(), responseData.getExplanation());
                }
                else {
                    return responseData.getProbability();
                }
            }
        }
        // DB에 해당 URL에 대한 값이 하나라도 없는 경우 (확률, 낚시성 의심 문장, 해설 중에서 하나라도 없는 경우)
        else {
            WebScrappingResultDto webScrappingResultDto = webScraping(url);

            String result = pythonFileRun_3(requestScriptPath, webScrappingResultDto.getTitle(), webScrappingResultDto.getContent());
            ModelDataDto modelDataDto = parseResult(result);

            log.info("Dto.probability: {}", modelDataDto.getProbability());
            log.info("Dto.sentence: {}", modelDataDto.getSentence());

            // 확률 값에 따라 반환
            if (modelDataDto.getProbability() > 0.5) {
                String explanation = openAI(webScrappingResultDto.getContent(), modelDataDto.getSentence());

                // mongodb에 값 저장
                ResponseData data = new ResponseData(
                        url,
                        modelDataDto.getProbability(),
                        modelDataDto.getSentence(),
                        explanation
                );

                responseDataRepository.save(data);

                return new PrecisionMeasurementDto(modelDataDto.getProbability(), modelDataDto.getSentence(), explanation);
            } else {
                // mongodb에 값 저장
                ResponseData data = new ResponseData(
                        url,
                        modelDataDto.getProbability(),
                        modelDataDto.getSentence(),
                        ""
                );

                responseDataRepository.save(data);

                return modelDataDto.getProbability();
            }
        }
    }

    // python 모델이 반환하는 값 분리하는 함수
    private ModelDataDto parseResult(String result) {
        // 문자열의 앞뒤 괄호 제거 및 공백 제거
        log.info(result);
        String trimmedResult = result.substring(1, result.length() - 1).trim();
        log.info("trimmedResult: {}", trimmedResult);

        // 첫 번째 콤마(,) 위치 찾기
        int firstCommaIndex = trimmedResult.indexOf(",");
        log.info("firstCommaIndex: {}", firstCommaIndex);

        // 실수형 확률 추출
        double probability = Double.parseDouble(trimmedResult.substring(0, firstCommaIndex).trim());
        log.info("probability: {}", probability);

        // 문자열 리스트 추출 (리스트는 대괄호로 둘러싸여 있음)
        String messagesString = trimmedResult.substring(firstCommaIndex + 1).trim();
        log.info("messagesString: {}", messagesString);
        messagesString = messagesString.substring(1, messagesString.length() - 1); // 대괄호 제거
        log.info("messagesString: {}", messagesString);

        // DTO 생성 및 반환
        return new ModelDataDto(probability, messagesString);
    }

    // openAI API Key를 사용하여 해설을 생성하는 python 파일을 실행하는 함수
    public String openAI(String content, String sentence) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("python3", openAiScriptPath, content, sentence);
        Process process = processBuilder.start();
        log.info("Process: {}", process);

        // 실행 결과 가져오기
        InputStream inputStream = process.getInputStream();
        log.info("InputStream: {}", inputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        log.info("BufferedReader: {}", reader);

        // 실행결과 저장
        StringBuilder resultBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            resultBuilder.append(line).append(System.lineSeparator());
        }

        log.info("openAI resultBuilder: {}", resultBuilder);

        // 실행 결과 파싱
        return resultBuilder.toString().trim();
    }

    // 사용자 정의 측정
    public Object customMeasurement(String content) throws IOException{
        log.info(content);

        StringBuilder resultString = new StringBuilder();

        boolean titleNotExist = true;
        String title = "";

        List<String> geted = List.of(content.split(","));
        String type = "";
        for (String item : geted) {
            if (type.isEmpty()) {
                type = item;
                continue;
            }
            if (type.equals("image")) {
                // 이미지 처리
                resultString.append(ocr(item)).append(" ");
            }
            else  {
                // 텍스트 처리
                if (titleNotExist) {
                    title = item;
                    titleNotExist = false;
                }
                resultString.append(item).append(" ");
            }
            type = "";
        }

        String result = pythonFileRun_3(requestScriptPath, title, resultString.toString().trim());
        ModelDataDto modelDataDto = parseResult(result);

        log.info("Dto.probability: {}", modelDataDto.getProbability());
        log.info("Dto.sentence: {}", modelDataDto.getSentence());

        // 확률 값에 따라 반환
        if (modelDataDto.getProbability() > 0.5) {
            return new PrecisionMeasurementDto(modelDataDto.getProbability(), modelDataDto.getSentence(), openAI(content, modelDataDto.getSentence()));
        } else {
            return modelDataDto.getProbability();
        }
    }

    // image 링크를 받아서 ocr를 통해 텍스트 추출
    public String ocr(String imageURL) throws IOException {
        return pythonFileRun_2(ocrScriptPath, imageURL);
    }

    // 웹 스크래핑 - 제목 & 본문 가져오기
    public WebScrappingResultDto webScraping(String url) throws IOException {
        log.info(url);
        log.info("Chrome Driver Info: {}", driver);

        if (!ObjectUtils.isEmpty(driver)) {
            driver.get(url);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // 페이지 전체가 넘어올 때까지 대기(5초)
            log.info("Chrome Driver Info: {}", driver);

            // 제목 추출
            String title = driver.getTitle();
            log.info("WebPage Title: {}", title);

            // 본문 내용 추출
            String content;

            // 입력 URL에서 대표 URL 부분 추출
            String domain = extractDomain(url);
            log.info("baseUrl: {}", domain);

            String matchedValue = findMatchingValue(urlToXpathMap, domain);
            log.info("matchedValue: {}", matchedValue);

            // 대표 URL이 HashMap에 존재하는지 확인: 기사인 경우
            if (matchedValue != null) {
                // 존재하면 해당하는 xpath를 가져옴
                String xpath = urlToXpathMap.get(domain);
                log.info("Found xpath: " + xpath);

                WebElement webElement = driver.findElement(By.xpath(xpath));
                content = webElement.getText();
                log.info("WebPage Content using xpath: {}", content);

                return new WebScrappingResultDto(title, content);

            } else if (domain.equals("blog.naver.com") || domain.equals("m.blog.naver.com")) {
                // 대표 URL이 naver blog인 경우
                WebElement webElement = driver.findElement(By.className("se-main-container"));

                // 모든 <div> 요소를 찾기
                List<WebElement> divElements = webElement.findElements(By.tagName("div"));

                // 마지막 <div> 요소를 제외한 나머지 내용 가져오기
                StringBuilder contentBuilder = new StringBuilder();
                for (int i = 0; i < divElements.size() - 1; i++) {
                    contentBuilder.append(divElements.get(i).getText()).append("\n");
                }

                content = contentBuilder.toString();
                log.info("WebPage Content using classname: {}", content);

                return new WebScrappingResultDto(title, content);

            } else if (domain.contains("tistory.com")) {
                // 대표 URL이 tistory blog인 경우
                try {
                    WebElement webElement = driver.findElement(By.className("contents_style"));
                    content = webElement.getText();
                    log.info("WebPage Content using classname: {}", content);
                } catch (NoSuchElementException e) {
                    WebElement webElement = driver.findElement(By.className("tt_article_useless_p_margin"));
                    content = webElement.getText();
                    log.info("WebPage Content using classname: {}", content);
                }

                return new WebScrappingResultDto(title, content);

            } else if (domain.equals("www.instagram.com")) {
                // 대표 URL이 인스타그램인 경우
                url = driver.getCurrentUrl();
                (new WebDriverWait(driver, Duration.ofSeconds(2))).until(driver -> true);

                // 모든 이미지 src를 추출하여 리스트에 저장
                StringBuilder result = new StringBuilder();

                boolean has_multiple_images = url.contains("img_index=");

                url = url.split("img_index=")[0];
                driver.get(url + "img_index=1");

                for (int i = 2;; i++) {
                    log.info("Image URL with index " + (i - 1));

                    WebElement webElement = driver.findElement(By.tagName("article"));
                    WebElement imageElement = webElement.findElement(By.tagName("img"));
                    String img = imageElement.getAttribute("src");
                    log.info("Image: {}", img);

                    result.append(ocr(img)).append(" ");
                    log.info("Result: {}", result);

                    if (!has_multiple_images) {
                        break;
                    }

                    driver.get(url + "img_index=" + i);
                    (new WebDriverWait(driver, Duration.ofSeconds(2))).until(driver -> true);

                    if (driver.getCurrentUrl().endsWith("=1")) {
                        break;
                    }
                }

                content = result.toString().trim();
                log.info("WebPage Content using classname: {}", content);

                return new WebScrappingResultDto(title, content);

            } else {
                log.info("This Service doesn't support This Web Page");
                return null;
            }
        } else {
            log.info("Web Driver does not exist");
            return null;
        }
    }

    // 입력된 URL에서 프로토콜을 제거하고 도메인을 추출하는 메서드
    private static String extractDomain(String url) {
        if (url.startsWith("https://")) {
            url = url.substring(8);
        } else if (url.startsWith("http://")) {
            url = url.substring(7);
        }
        // 도메인 부분 추출
        int endIndex = url.indexOf('/');
        return endIndex != -1 ? url.substring(0, endIndex) : url;
    }

    // 가장 많이 일치하는 URL 키를 찾는 메서드
    private static String findMatchingValue(Map<String, String> urlMap, String domain) {
        for (String key : urlMap.keySet()) {
            if (domain.contains(key)) {
                return urlMap.get(key);
            }
        }
        return null;
    }

}
