package com.allan.springBootBoard.web.board.domain.model;

import com.allan.springBootBoard.web.member.domain.Gender;
import com.allan.springBootBoard.web.board.domain.Address;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberDTO {
    private Long memberId;
    private String id;
    private String password;
    private String name;
    private int age;
    private Gender gender;
    private Address address;
}
