package com.allan.springBootBoard.web.member.exception;

import com.allan.springBootBoard.web.error.code.ErrorCode;
import com.allan.springBootBoard.web.error.exception.BusinessException;

public class SameIdUseException extends BusinessException {
    public SameIdUseException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
