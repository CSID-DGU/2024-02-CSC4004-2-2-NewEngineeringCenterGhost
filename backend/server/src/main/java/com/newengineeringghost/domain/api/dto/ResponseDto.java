package com.newengineeringghost.domain.api.dto;

public class ResponseDto {
    private String probability;
    private String sentenceLength;
    private String sentencePosition;
    private String explanation;

    public ResponseDto(String probability, String sentenceLength, String sentencePosition, String explanation) {
        this.probability = probability;
        this.sentenceLength = sentenceLength;
        this.sentencePosition = sentencePosition;
        this.explanation = explanation;
    }
}