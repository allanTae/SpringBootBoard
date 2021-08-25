package com.allan.springBootBoard.web.error.exception;

import com.allan.springBootBoard.web.error.code.ErrorCode;

public class MemberNotFoundException extends BusinessException{
    public MemberNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
