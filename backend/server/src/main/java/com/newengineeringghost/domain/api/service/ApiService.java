package com.newengineeringghost.domain.api.service;

import com.newengineeringghost.domain.api.dto.ModelDataDto;
import com.newengineeringghost.domain.api.dto.PrecisionMeasurementDto;
import com.newengineeringghost.domain.api.dto.ResponseDataDto;
import com.newengineeringghost.domain.api.entity.ResponseData;
import com.newengineeringghost.domain.api.repository.ResponseDataRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
        urlToXpathMap.put("https://news.kbs.co.kr/news/pc/view","//*[@id=\"cont_newstext\"]"); // KBS 뉴스
        urlToXpathMap.put("https://www.hankyung.com/article","//*[@id=\"articletxt\"]"); //한국경제
        urlToXpathMap.put("https://imnews.imbc.com/news","//*[@id=\"content\"]/div/section[1]/article/div[2]/div[4]"); // MBC 뉴스
        urlToXpathMap.put("https://www.ohmynews.com/NWS_Web/View","//*[@id=\"content_wrap\"]/div[1]/div[3]/div[1]/div[1]/div[1]/div"); // 오마이뉴스
        urlToXpathMap.put("https://www.mk.co.kr/news","//*[@id=\"container\"]/section/div[3]/section/div[1]/div[1]/div[1]"); // 매일경제
        urlToXpathMap.put("https://www.dailian.co.kr/news/view","//*[@id=\"contentsArea\"]/div[1]/div[4]/div[2]/div[2]/description"); // 데일리안
        urlToXpathMap.put("https://www.nocutnews.co.kr/news","//*[@id=\"pnlContent\"]"); // 노컷뉴스
        urlToXpathMap.put("https://view.asiae.co.kr/article","//*[@id=\"txt_area\"]"); // 아시아경제
        urlToXpathMap.put("https://www.edaily.co.kr/News","//*[@id=\"contents\"]/section[1]/section[1]/div[1]/div[3]/div[1]"); // 이데일리
        urlToXpathMap.put("https://biz.heraldcorp.com/article","//*[@id=\"articleText\"]"); // 해럴드경제
        urlToXpathMap.put("https://zdnet.co.kr/view","//*[@id=\"articleBody\"]");
        urlToXpathMap.put("https://www.seoul.co.kr/news","//*[@id=\"articleContent\"]/div"); // 서울신문
        urlToXpathMap.put("https://www.osen.co.kr/article","//*[@id=\"articleBody\"]"); // OSEN
        urlToXpathMap.put("https://news.sbs.co.kr/news","//*[@id=\"container\"]/div[1]/div[3]/div[2]/div[1]/div[1]/div[2]"); // SBS 뉴스
        urlToXpathMap.put("https://newstapa.org/article","//*[@id=\"editor_fontsize\"]"); //뉴스타파
        urlToXpathMap.put("https://www.hankookilbo.com/News/Read","/html/body/div[2]/div/div[4]/div/div[1]"); // 한국일보
        urlToXpathMap.put("https://isplus.com/article/view","//*[@id=\"article_body\"]"); // 일간스포츠
        urlToXpathMap.put("https://www.newsis.com/view","//*[@id=\"content\"]/div[1]/div[1]/div[3]/article"); // 뉴시스
        urlToXpathMap.put("https://www.inews24.com/view","//*[@id=\"articleBody\"]"); // 아이뉴스
        urlToXpathMap.put("https://mydaily.co.kr/page/view","//*[@id=\"container\"]/div[1]/div[4]/div[1]/div"); // 마이데일리
        urlToXpathMap.put("https://www.donga.com/news/NewsStand/article","//*[@id=\"contents\"]/div[2]/div/div[1]/section[1]"); // 동아일보
        urlToXpathMap.put("https://news.jtbc.co.kr/article","//*[@id=\"wrapper\"]/div/div[6]/div/div/div/div/div[1]/div[1]"); // JTBC 뉴스
        urlToXpathMap.put("https://www.ytn.co.kr/","//*[@id=\"CmAdContent\"]/span"); // ytn 뉴스
        urlToXpathMap.put("https://www.newdaily.co.kr/site/data/html","//*[@id=\"article_conent\"]"); // 뉴데일리
        urlToXpathMap.put("https://www.dt.co.kr/contents","//*[@id=\"v-left-scroll-in\"]/div[2]/div/div[2]"); // 디지털타임스
        urlToXpathMap.put("https://sports.chosun.com/","//*[@id=\"articleBody\"]/div/div/font"); // 스포츠조선
        urlToXpathMap.put("https://www.chosun.com/","//*[@id=\"fusion-app\"]/div[1]/div[2]/div/section/article/section"); // 조선일보
        urlToXpathMap.put("https://www.sportsseoul.com/news/read","//*[@id=\"article-body\"]"); // 스포츠서울
        urlToXpathMap.put("https://www.khan.co.kr/article","//*[@id=\"articleBody\"]"); // 경향신문
        urlToXpathMap.put("https://sports.donga.com/NewsStand/article","//*[@id=\"article_body\"]"); // 스포츠동아
        urlToXpathMap.put("https://news.mt.co.kr/mtview","//*[@id=\"textBody\"]"); // 머니투데이
        urlToXpathMap.put("https://www.etnews.com/","//*[@id=\"articleBody\"]/p"); // 전자신문
        urlToXpathMap.put("https://www.sedaily.com/NewsView","//*[@id=\"v-left-scroll-in\"]/div[2]/div[1]/div[2]"); // 서울경제
        urlToXpathMap.put("https://www.joongang.co.kr/article","//*[@id=\"article_body\"]"); // 중앙일보
        urlToXpathMap.put("https://www.sisain.co.kr/news/article","//*[@id=\"article-view-content-div\"]"); // 시사인
        urlToXpathMap.put("https://www.wowtv.co.kr/NewsCenter/News/Read","//*[@id=\"divNewsContent\"]"); // 한국경제TV
        urlToXpathMap.put("https://www.yonhapnewstv.co.kr/news/","//*[@id=\"articleBody\"]"); // 연합뉴스
        urlToXpathMap.put("https://www.kmib.co.kr/article/","//*[@id=\"articleBody\"]"); // 국민일보
        urlToXpathMap.put("https://www.mbn.co.kr/news","//*[@id=\"newsViewArea\"]"); // MBN 뉴스
        urlToXpathMap.put("https://biz.chosun.com/","//*[@id=\"fusion-app\"]/div[1]/div[2]/div/section/article/section"); // 조선비즈
        urlToXpathMap.put("https://www.segye.com/newsView","//*[@id=\"wps_layout1_box2\"]"); // 세계일보
        urlToXpathMap.put("https://www.fnnews.com/news","//*[@id=\"article_content\"]"); // 파이낸셜 뉴스
        urlToXpathMap.put("https://sportalkorea.com/news/","//*[@id=\"CmAdContent\"]/div[2]/div/div"); // 스포탈코리아
        urlToXpathMap.put("https://www.hani.co.kr/arti","//*[@id=\"renewal2023\"]/span"); // 한겨레
        urlToXpathMap.put("https://n.news.naver.com/article","//*[@id=\"dic_area\"]"); // 네이버뉴스
    }

//    private ClassifyUrlRepository classifyUrlRepository;

    private ResponseDataRepository responseDataRepository;

//    @Autowired
//    public void setClassifyUrlRepository(ClassifyUrlRepository classifyUrlRepository) {this.classifyUrlRepository = classifyUrlRepository;}

    @Autowired
    public void setResponseDataRepository(ResponseDataRepository responseDataRepository) {this.responseDataRepository = responseDataRepository;}

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

    // 빠른 측정
    public double quickMeasurement(String url) throws IOException {
        String content = webScraping(url);

        ProcessBuilder processBuilder = new ProcessBuilder("python3", requestScriptPath, content);
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

        String[] parts = resultBuilder.toString().split("[(),]");

        String doubleString = parts[1].trim();

        double probability = Double.parseDouble(doubleString);
        log.info("Probability: {}", probability);

        return probability;
    }

    // python 모델이 반환하는 값 분리하는 함수
    private ModelDataDto parseResult(String result) {
        // 문자열의 앞뒤 괄호 제거 및 공백 제거
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

    // 정밀 측정
    public Object precisionMeasurement(String url) throws IOException {
        String content = webScraping(url);

        ProcessBuilder processBuilder = new ProcessBuilder("python3", requestScriptPath, content);
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

        // 실행 결과 파싱
        String result = resultBuilder.toString().trim();
        ModelDataDto modelDataDto = parseResult(result);

        log.info("Dto.probability: {}", modelDataDto.getProbability());
        log.info("Dto.sentence: {}", modelDataDto.getSentence());

        String explanation = openAI(content, modelDataDto.getSentence());

        // 확률 값에 따라 반환
        if (modelDataDto.getProbability() > 0.5) {
            // Todo : mongodb 저장
            return new PrecisionMeasurementDto(modelDataDto.getProbability(), modelDataDto.getSentence(), explanation);
        } else {
            // Todo : mongodb 저장
            return modelDataDto.getProbability();
        }
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
        List<String> imageUrls = new ArrayList<>();
        List<String> texts = new ArrayList<>();
        log.info(content);

        List<String> geted = List.of(content.split(","));
        String type = "";
        for (String item : geted) {
            if (type == "") {
                type = item;
                continue;
            }
            if (type == "image") {
                imageUrls.add(item);
            }
            else  {
                texts.add(item);
            }
            type = "";
        }

        List<String> ocrResult = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            ocrResult.add(ocr(imageUrl));
        }

        StringBuilder resultString = new StringBuilder();
        for (String text : texts) {
            resultString.append(text).append(" ");
        }
        for (String ocr : ocrResult) {
            resultString.append(ocr).append(" ");
        }

        // text 처리
        ProcessBuilder processBuilder = new ProcessBuilder("python3", requestScriptPath, resultString.toString().trim());
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

        // 실행 결과 파싱
        String result = resultBuilder.toString().trim();
        ModelDataDto modelDataDto = parseResult(result);

        log.info("Dto.probability: {}", modelDataDto.getProbability());
        log.info("Dto.sentence: {}", modelDataDto.getSentence());

        // 확률 값에 따라 반환
        if (modelDataDto.getProbability() > 0.5) {
            // Todo : mongodb 저장
            return new PrecisionMeasurementDto(modelDataDto.getProbability(), modelDataDto.getSentence(), openAI(content, modelDataDto.getSentence()));
        } else {
            // Todo : mongodb 저장
            return modelDataDto.getProbability();
        }
    }

    // image 링크를 받아서 ocr를 통해 텍스트 추출
    public String ocr(String imageURL) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("python3", ocrScriptPath, imageURL);
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

//    public Optional<String> getXpathByUrl(String url) {
//        List<ClassifyUrl> classifyUrls = classifyUrlRepository.findByUrlStartingWith(url);
//
//        if (classifyUrls.isEmpty()) {
//            return Optional.empty();
//        }
//
//        // 입력된 URL과 저장된 URL의 길이를 비교하여 가장 긴 URL을 선택합니다.
//        ClassifyUrl matchedUrl = classifyUrls.stream()
//                .filter(classifyUrl -> url.startsWith(classifyUrl.getUrl()))
//                .max((a, b) -> Integer.compare(a.getUrl().length(), b.getUrl().length()))
//                .orElse(null);
//
//        if (matchedUrl == null) {
//            return Optional.empty();
//        }
//
//        return Optional.of(matchedUrl.getXpath());
//    }

    // 입력 URL에서 기본 URL 부분을 추출하는 메서드
    private static String extractBaseUrl(String url) {
        // URL에서 숫자 앞까지 잘라내는 방법 (예시: /article/028까지)
        int lastSlashIndex = url.lastIndexOf('/');
        return url.substring(0, lastSlashIndex);
    }

    public String webScraping(String url) throws IOException {
        log.info(url);
        log.info("Chrome Driver Info: {}", driver);

        if (!ObjectUtils.isEmpty(driver)) {
            driver.get(url);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(100)); // 페이지 전체가 넘어올 때까지 대기(100초)
            log.info("Chrome Driver Info: {}", driver);

            // 제목 추출
            String title = driver.getTitle();
            log.info("WebPage Title: {}", title);

            // 본문 내용 추출
            String content;

            // 입력 URL에서 대표 URL 부분 추출
            String baseUrl = extractBaseUrl(url);
            // 대표 URL이 HashMap에 존재하는지 확인
            if (urlToXpathMap.containsKey(baseUrl)) {
                // 존재하면 해당하는 xpath를 가져옴
                String xpath = urlToXpathMap.get(baseUrl);
                log.info("Found xpath: " + xpath);

                WebElement webElement = driver.findElement(By.xpath(xpath));
                content = webElement.getText();
                log.info("WebPage Content using xpath: {}", content);

                StringBuilder result = new StringBuilder();
                result.append(title).append(".");
                result.append(content);
                log.info("WEB SCRAPING RESULT: {}", result);

                return result.toString();
            } else {
                return "지원되지 않는 사이트입니다.";
            }
        } else {
            return "";
        }
    }

    // 웹 페이지에서 제목&본문 추출
//    public String webScraping(String url) throws IOException {
//        log.info(url);
//        log.info("Chrome Driver Info: {}", driver);
//
//        if (!ObjectUtils.isEmpty(driver)) {
//            driver.get(url);
//            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(100)); // page 전체가 넘어올 때까지 대기(5초)
//            log.info("Chrome Driver Info: {}", driver);
//
//            // 제목 추출
//            String title = driver.getTitle();
//            log.info("WebPage Title: {}", title);
//
//            // 본문 내용 추출
//            String content;
//            try {
//                // 시도 1: <article> 요소 찾기
//                WebElement webElement2 = driver.findElement(By.tagName("article"));
//                content = webElement2.getText();
//                log.info("WebPage Content from <article>: {}", content);
//            } catch (NoSuchElementException e) {
//                // 시도 2: <article> 요소가 없을 경우 모든 <span> 요소 찾기
//                List<WebElement> spanElements = driver.findElements(By.tagName("span"));
//                StringBuilder contentBuilder = new StringBuilder();
//                for (WebElement span : spanElements) {
//                    contentBuilder.append(span.getText()).append("\n");
//                }
//                content = contentBuilder.toString();
//                log.info("WebPage Content from all <span>: {}", content);
//            }
//
//            StringBuilder result = new StringBuilder();
//            result.append(title);
//            result.append(".");
//            result.append(content);
//            log.info("WEB SCRAPING RESULT: {}", result);
//
//            return result.toString();
//        } else {
//            return "";
//        }
//    }

    // 웹 페이지에서 이미지 추출
    public List<String> webScrapingImage(String url) throws IOException {
        log.info("Chrome Driver Info: {}", driver);

        if (!ObjectUtils.isEmpty(driver)) {
            driver.get(url);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(100)); // page 전체가 넘어올 때까지 대기(5초)
            log.info("Chrome Driver Info: {}", driver);

            // 이미지 추출
            WebElement articleElement = driver.findElement(By.tagName("article"));
            List<WebElement> imageElements = articleElement.findElements(By.tagName("img"));

            // 모든 이미지 src를 추출하여 리스트에 저장
            List<String> imgSrcs = new ArrayList<>();
            for (WebElement imageElement : imageElements) {
                String img = imageElement.getAttribute("src");
                imgSrcs.add(img);
                log.info("Image: {}", img);
            }

            // 리스트를 문자열로 반환 (필요에 따라 적절히 수정 가능)
            return imgSrcs;
        } else {
            List<String> imgSrcs = new ArrayList<>();
            String img = "no element";
            imgSrcs.add(img);
            log.info("Image: {}", imgSrcs);

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
