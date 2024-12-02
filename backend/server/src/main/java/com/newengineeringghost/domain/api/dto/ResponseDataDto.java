package com.newengineeringghost.domain.api.dto;

import lombok.Getter;

@Getter
public class ResponseDataDto {
    String link;
    double probability;
    String fishingSentence;
    String explanation;

    public ResponseDataDto(String link, double probability, String fishingSentence, String explanation) {
        this.link = link;
        this.probability = probability;
        this.fishingSentence = fishingSentence;
        this.explanation = explanation;
    }
}
