package com.allan.springBootBoard.web.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberRole {

    USER("ROLE_USER", "normal"),
    ADMIN("ROLE_ADMIN", "manager"),
    GOOGLE("ROLE_GOOGLE", "google"),
    NAVER("ROLE_NAVER", "naver"),
    KAKAO("ROLE_KAKAO", "kakao");

    private String key;
    private String title;

    public static MemberRole valueOfTitle(String title){
        switch(title){
            case "normal":
                return USER;
            case "manager":
                return ADMIN;
            case "google":
                return GOOGLE;
            case "naver":
                return NAVER;
            case "kakao":
                return KAKAO;
            default:
                throw new RuntimeException("존재하지 않는 회원 유형입니다.");
        }
    }

}
