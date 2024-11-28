package com.newengineeringghost.domain.api.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "data")
@Getter
@NoArgsConstructor
public class ResponseData {
    @Id
    private String id;
    private String link;
    private double probability;
    private String sentencePosition;
    private String sentenceLength;
    private String explanation;

    public ResponseData(
            String link,
            double probability,
            String sentencePosition,
            String sentenceLength,
            String explanation) {
        this.link = link;
        this.probability = probability;
        this.sentencePosition = sentencePosition;
        this.sentenceLength = sentenceLength;
        this.explanation = explanation;
    }
}
