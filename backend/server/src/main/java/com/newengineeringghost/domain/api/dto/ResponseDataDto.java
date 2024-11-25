package com.newengineeringghost.domain.api.dto;

import lombok.Getter;

@Getter
public class ResponseDataDto {
    String link;
    String probability;
    String sentenceLength;
    String sentencePosition;
    String explanation;

    public ResponseDataDto(String link, String probability, String sentenceLength, String sentencePosition, String explanation) {
        this.link = link;
        this.probability = probability;
        this.sentenceLength = sentenceLength;
        this.sentencePosition = sentencePosition;
        this.explanation = explanation;
    }
}