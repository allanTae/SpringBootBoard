package com.allan.springBootBoard.web.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum LoginErrorCode{
    SAMPLE(100,"sampleMessage");

    @Setter
    @Getter
    private int code;

    @Setter
    @Getter
    private String description;
}
