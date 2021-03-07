package com.allan.springBootBoard.web.member.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Gender {
    MAN(1, "남자"), WOMAN(2, "여자");

    private String description;
    private int id;

    Gender(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public static Gender valueOf(int id){
        switch (id){
            case 1: return MAN;
            case 2: return WOMAN;
            default: throw new AssertionError("Unknown id: " + id);
        }
    }

}
