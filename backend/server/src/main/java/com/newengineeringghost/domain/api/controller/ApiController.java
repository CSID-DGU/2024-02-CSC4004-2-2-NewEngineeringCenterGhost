package com.newengineeringghost.domain.api.controller;

import com.newengineeringghost.domain.api.dto.ResponseDataDto;
import com.newengineeringghost.domain.api.service.ApiService;
import com.newengineeringghost.domain.api.service.MongoDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/server")
public class ApiController {
    private ApiService apiService;

    private MongoDbService mongoDbService;

    @Autowired
    public void setApiService(ApiService apiService) {this.apiService = apiService;}

    @Autowired
    public void setMongoDbService(MongoDbService mongoDbService) {this.mongoDbService = mongoDbService;}

    // 빠른 측정
    @PostMapping("/quick")
    public double quickMeasurement(@RequestBody Map<String, String> request) throws IOException {
        return apiService.quickMeasurement(request.get("url"));
    }

    // 정밀 측정
    @PostMapping("/precision")
    public Object precisionMeasurement(@RequestBody Map<String, String> request) throws IOException {
        return apiService.precisionMeasurement(request.get("url"));
    }

    // 사용자 정의 측정
    @PostMapping("/custom")
    public Object customMeasurement(@RequestBody Map<String, String> request) throws IOException {
        return apiService.customMeasurement(request.get("content"));
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
        return mongoDbService.getData(link);
    }

    // mongodb 통신 test
    @PostMapping("/post")
    public String postMeasurement(@RequestBody ResponseDataDto responseDto) {
        return mongoDbService.postData(responseDto);
    }
}