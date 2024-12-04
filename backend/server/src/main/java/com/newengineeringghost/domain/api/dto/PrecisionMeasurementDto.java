package com.newengineeringghost.domain.api.dto;

import lombok.Getter;

@Getter
public class PrecisionMeasurementDto {
    double probability;
    String sentence;
    String explanation;

    public PrecisionMeasurementDto(double probability, String sentence, String explanation) {
        this.probability = probability;
        this.sentence = sentence;
        this.explanation = explanation;
    }
}