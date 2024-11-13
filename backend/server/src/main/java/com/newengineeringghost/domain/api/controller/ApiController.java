package com.newengineeringghost.domain.api.controller;

import com.newengineeringghost.domain.api.dto.ResponseDataDto;
import com.newengineeringghost.domain.api.dto.WebContentsDto;
import com.newengineeringghost.domain.api.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/")
public class ApiController {
    private ApiService apiService;

    @Autowired
    public void setApiService(ApiService apiService) {this.apiService = apiService;}

    // 빠른 측정
    @PostMapping("/quick")
    public String quickMeasurement(@RequestParam String url) throws IOException {
        return apiService.quickMeasurement(url);
    }

    // 정밀 측정
    @PostMapping("/precision")
    public String precisionMeasurement(@RequestParam String url) throws IOException {
        return apiService.precisionMeasurement(url);
    }

    // 사용자 정의 측정
    @PostMapping("/custom")
    public String customMeasurement(@RequestParam String url) throws IOException {
        return apiService.customMeasurement(url);
    }

    // 웹 크롤링 테스트
    @PostMapping("/test")
    public WebContentsDto testMeasurement(@RequestParam String url) throws IOException {
        return apiService.webCrawling(url);
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