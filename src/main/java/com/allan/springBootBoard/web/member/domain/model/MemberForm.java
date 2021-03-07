package com.allan.springBootBoard.web.member.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "아이디를 입력 해 주세요.")
    private String memberId;

    @NotEmpty(message = "비밀번호를 입력 해 주세요.")
    private String pwd;

    private String re_pwd;

    @NotEmpty(message = "이름을 입력 해 주세요.")
    private String name;

    @NotEmpty(message = "도로명을 입력 해 주세요.")
    private String street;

    @NotEmpty(message = "우편번호를 입력 해 주세요.")
    private String zipcode;

    @NotEmpty(message = "도시명을 입력 해 주세요.")
    private String city;

    @NotEmpty(message = "전화번호를 입력 해 주세요.")
    private String phone;

    @NotEmpty(message = "성별을 선택 해 주세요.")
    private String gender;

    @Max(message = "나이를 제대로 입력 해 주세요.", value = 150L)
    @Min(message = "나이를 제대로 입력 해 주세요.", value = 0L)
    private Long age;

}
