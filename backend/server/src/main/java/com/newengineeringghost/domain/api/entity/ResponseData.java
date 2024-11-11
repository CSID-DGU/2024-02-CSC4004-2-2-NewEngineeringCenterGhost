package com.newengineeringghost.domain.api.entity;

import com.newengineeringghost.domain.api.dto.ResponseDto;
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
    private String probability;
    private String sentencePosition;
    private String sentenceLength;
    private String explanation;
}
