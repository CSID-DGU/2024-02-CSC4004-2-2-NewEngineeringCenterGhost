package com.newengineeringghost.domain.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString // ToString 메서드 자동 생성: 객체가 가지고 있는 정보나 값들을 문자열로 만들어 리턴
@Builder // 값을 주입하여 객체를 만드는 용도
public class WebContentsDto {
    String title;
    String bodyContent;

    public WebContentsDto(String title, String bodyContent) {
        this.title = title;
        this.bodyContent = bodyContent;
    }
}
