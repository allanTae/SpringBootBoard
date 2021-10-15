package com.allan.springBootBoard.web.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 프론트단에 현재 로그인한 회원의 이름 및 아이디를 전달하기 위한 DTO
 */

@Getter
@Setter
@AllArgsConstructor
public class UserInfo {
    String user_name;
    String authId;
}
