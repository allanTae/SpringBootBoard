package com.allan.springBootBoard.web.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JoinType {
    NORMAL("service", "system", "일반 서비스를 이용한 가입유형"),
    GOOGLE("social", "google", "google oauth2 를 이용한 가입유형"),
    NAVER("social", "naver", "naver oauth2 를 이용한 가입유형"),
    KAKAO("social", "kakao", "kakao oauth2 를 이용한 가입유형");
    private String type; // 가입유형
    private String detail; // 세부 가입 종류
    private String description;
}
