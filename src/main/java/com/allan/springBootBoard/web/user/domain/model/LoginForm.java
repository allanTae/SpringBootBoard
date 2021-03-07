package com.allan.springBootBoard.web.user.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.SecondaryTable;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LoginForm {

    @NotEmpty(message="아이디를 입력해주세요.")
    private String userId;

    @NotEmpty(message="비밀번호를 입력해주세요.")
    private String userPwd;
    private boolean useCookie;

}
