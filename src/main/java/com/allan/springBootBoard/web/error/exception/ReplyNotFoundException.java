package com.allan.springBootBoard.web.error.exception;

import com.allan.springBootBoard.web.error.code.ErrorCode;

public class ReplyNotFoundException extends BusinessException{
    public ReplyNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
