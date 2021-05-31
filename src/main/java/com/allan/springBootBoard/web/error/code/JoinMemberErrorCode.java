package com.allan.springBootBoard.web.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum JoinMemberErrorCode {
    IN_USE_ID(100, "id is already in use.");

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String message;
}
