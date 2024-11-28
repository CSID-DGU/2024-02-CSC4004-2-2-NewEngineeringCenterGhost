package com.newengineeringghost.domain.api.dto;

import lombok.Getter;

@Getter
public class ResponseDataDto {
    String link;
    double probability;
    String sentencePosition;
    String sentenceLength;
    String explanation;

    public ResponseDataDto(String link, double probability, String sentencePosition, String sentenceLength, String explanation) {
        this.link = link;
        this.probability = probability;
        this.sentencePosition = sentencePosition;
        this.sentenceLength = sentenceLength;
        this.explanation = explanation;
    }
}
