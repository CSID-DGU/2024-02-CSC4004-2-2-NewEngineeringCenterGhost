package com.newengineeringghost.domain.api.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "responseData")
public class ResponseData {

    @Id
    private String id;
    private String probaility;
    private String sentencePostion;
    private String sentenceLength;
    private String explanation;
}
