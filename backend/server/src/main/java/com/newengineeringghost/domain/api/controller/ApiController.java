package com.newengineeringghost.domain.api.controller;

import com.newengineeringghost.domain.api.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}