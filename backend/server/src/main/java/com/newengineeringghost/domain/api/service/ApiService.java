package com.newengineeringghost.domain.api.service;

import com.newengineeringghost.domain.api.dto.ResponseDto;
import com.newengineeringghost.domain.api.entity.ResponseData;
import com.newengineeringghost.domain.api.repository.ResponseDataRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class ApiService {

    private ResponseDataRepository responseDataRepository;

    @Autowired
    public void setResponseDataRepository(ResponseDataRepository responseDataRepository) {this.responseDataRepository = responseDataRepository;}

    /*
    Todo: 빠른 측정

    기능)
    사용자가 링크 위에 마우스를 올리면 서버로 해당 링크 전송
    서버에서 링크의 정보를 분석한 후, 낚시성 정보 포함 확률을 위에 바로 표시

    request : 측정할 페이지 링크
    response : 확률값
     */
    public String quickMeasurement(String url) throws IOException {
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

        // Todo : python 파일 실행결과 받아와서 Response data dto에 저장한 후, probability만 return

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
    python에 매개변수로 image를 넘겨주는 것보다
    python에서 beautifulsoup을 사용해서 image를 다운로드하는 게 속도 측면에서 낫지 않나?
     */
    public String downlaodImage(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements imgs = doc.select("img");

        System.out.println(imgs);
        return "";
    }

    public String downloadPage(String url) {

        return "";
    }

    /*
    Todo : 링크 분석 및 데이터 처리

    기능)
    사용자가 전송한 링크가 확장 프로그램에서 지원하는 사이트인지 확인
    지원되는 사이트인 경우, BeautifulSoup을 사용해 링크 내 데이터 추출
    추출된 데이터가 유효하다면, 해당 데이터를 '낚시성 정보 판별 기능'을 통해 분석하고,
    결과로 나온 확률값을 사용자에게 전송

    request :
    response :
     */

    // mongodb
    public ResponseDto getData(String link) {
        ResponseData responseData = responseDataRepository.findResponseDataByLink(link);

        ResponseDto responseDto = new ResponseDto(
                responseData.getLink(),
                responseData.getProbability(),
                responseData.getSentencePosition(),
                responseData.getSentenceLength(),
                responseData.getExplanation());

        return responseDto;
    }

    public String postData(ResponseDto responseDto) {

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
