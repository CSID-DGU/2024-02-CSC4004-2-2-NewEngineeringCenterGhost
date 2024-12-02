package com.newengineeringghost.domain.api.controller;

import com.newengineeringghost.domain.api.dto.ResponseDataDto;
import com.newengineeringghost.domain.api.service.ApiService;
import com.newengineeringghost.domain.api.service.MongoDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    // mongodb 통신 test
    @GetMapping("/get")
    public ResponseDataDto getMeasurement(@RequestParam String link) {
        return mongoDbService.getData(link);
    }

    // mongodb 통신 test
    @PostMapping("/post")
    public String postMeasurement(@RequestBody ResponseDataDto responseDto) {
        return mongoDbService.postData(responseDto);
    }
}