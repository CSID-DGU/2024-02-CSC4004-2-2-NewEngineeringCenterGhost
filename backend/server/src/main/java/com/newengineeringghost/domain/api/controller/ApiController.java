package com.newengineeringghost.domain.api.controller;

import com.newengineeringghost.domain.api.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/server")
public class ApiController {
    private ApiService apiService;

    @Autowired
    public void setApiService(ApiService apiService) {this.apiService = apiService;}

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
}