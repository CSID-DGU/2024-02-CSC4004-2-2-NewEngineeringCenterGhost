package com.newengineeringghost.domain.api.dto;

import lombok.Getter;

@Getter
public class ModelDataDto {
    double probability;
    String sentence;

    public ModelDataDto(double probability, String sentence) {
        this.probability = probability;
        this.sentence = sentence;
    }
}