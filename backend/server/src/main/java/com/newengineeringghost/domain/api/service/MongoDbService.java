package com.newengineeringghost.domain.api.service;

import com.newengineeringghost.domain.api.dto.ResponseDataDto;
import com.newengineeringghost.domain.api.entity.ResponseData;
import com.newengineeringghost.domain.api.repository.ResponseDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoDbService {
    private final ResponseDataRepository responseDataRepository;

    @Autowired
    public MongoDbService(ResponseDataRepository responseDataRepository) {
        this.responseDataRepository = responseDataRepository;
    }

    // mongodb 연동 test
    public ResponseDataDto getData(String link) {
        ResponseData responseData = responseDataRepository.findResponseDataByLink(link).orElse(null);

        if (responseData != null) {
            ResponseDataDto responseDto = new ResponseDataDto(
                    responseData.getLink(),
                    responseData.getProbability(),
                    responseData.getFishingSentence(),
                    responseData.getExplanation());

            return responseDto;
        } else {
            return null;
        }
    }

    // mongodb 연동 test
    public String postData(ResponseDataDto responseDto) {

        ResponseData responseData = new ResponseData(
                responseDto.getLink(),
                responseDto.getProbability(),
                responseDto.getFishingSentence(),
                responseDto.getExplanation()
        );

        responseDataRepository.save(responseData);

        return "success";
    }
}
