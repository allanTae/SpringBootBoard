package com.allan.springBootBoard.web.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberRole {

    USER("ROLE_USER", "user", JoinType.NORMAL),
    ADMIN("ROLE_ADMIN", "manager", JoinType.NORMAL),
    GOOGLE("ROLE_GOOGLE", "user", JoinType.GOOGLE),
    NAVER("ROLE_NAVER", "user", JoinType.NAVER),
    KAKAO("ROLE_KAKAO", "user", JoinType.KAKAO);

    private String key;
    private String role;
    private JoinType joinType; // 가입유형을 구분하기 위한 구분자.


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
