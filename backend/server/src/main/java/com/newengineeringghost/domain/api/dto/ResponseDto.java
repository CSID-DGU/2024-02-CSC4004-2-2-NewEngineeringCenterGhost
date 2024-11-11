package com.newengineeringghost.domain.api.dto;

import lombok.Getter;

@Getter
public class ResponseDto {
    private String link;
    private String probability;
    private String sentenceLength;
    private String sentencePosition;
    private String explanation;

    public ResponseDto(String link, String probability, String sentenceLength, String sentencePosition, String explanation) {
        this.link = link;
        this.probability = probability;
        this.sentenceLength = sentenceLength;
        this.sentencePosition = sentencePosition;
        this.explanation = explanation;
    }
}