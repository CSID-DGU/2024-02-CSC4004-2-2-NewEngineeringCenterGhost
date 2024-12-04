package com.newengineeringghost.domain.api.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "data")
@Getter
@Setter
@NoArgsConstructor
public class ResponseData {
    @Id
    private String id;
    private String link;
    private double probability;
    private String fishingSentence;
    private String explanation;

    public ResponseData(
            String link,
            double probability,
            String fishingSentence,
            String explanation) {
        this.link = link;
        this.probability = probability;
        this.fishingSentence = fishingSentence;
        this.explanation = explanation;
    }
}
