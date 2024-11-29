package com.newengineeringghost.domain.api.controller;

import com.newengineeringghost.domain.api.dto.ResponseDataDto;
import com.newengineeringghost.domain.api.service.ApiService;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/server")
public class ApiController {
    private ApiService apiService;

    @Autowired
    public void setApiService(ApiService apiService) {this.apiService = apiService;}

    // 빠른 측정
    @PostMapping("/quick")
    public double quickMeasurement(@RequestParam String url) throws IOException {
        return apiService.quickMeasurement(url);
    }

    // 정밀 측정
    @PostMapping("/precision")
    public Object precisionMeasurement(@RequestParam String url) throws IOException {
        return apiService.precisionMeasurement(url);
    }

    // 사용자 정의 측정
    @PostMapping("/custom")
    public Object customMeasurement(@RequestBody String content) throws IOException {
        return apiService.customMeasurement(content);
    }

    // 웹 스크래핑 테스트
    @PostMapping("/test")
    public String testMeasurement(@RequestParam String url) throws IOException {
        return apiService.webScraping(url);
    }

    // 웹 스크래핑(이미지) 테스트
    @PostMapping("/test/image")
    public List<String> testImage(@RequestParam String url) throws IOException {
        return apiService.webScrapingImage(url);
    }

    // mongodb 통신 test
    @GetMapping("/get")
    public ResponseDataDto getMeasurement(@RequestParam String link) throws IOException {
        return apiService.getData(link);
    }

    // mongodb 통신 test
    @PostMapping("/post")
    public String postMeasurement(@RequestBody ResponseDataDto responseDto) {
        return apiService.postData(responseDto);
    }
}