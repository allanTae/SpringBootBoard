package com.allan.springBootBoard.web.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 프론트단에 현재 로그인한 회원의 이름 및 아이디를 전달하기 위한 DTO
 */

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserInfo {
    String authId;
    String nickname;
}
