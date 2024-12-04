package com.newengineeringghost.domain.api.dto;

import lombok.Getter;

@Getter
public class WebScrappingResultDto {
    String title;
    String content;

    public WebScrappingResultDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}