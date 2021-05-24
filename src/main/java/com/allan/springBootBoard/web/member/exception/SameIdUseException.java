package com.allan.springBootBoard.web.member.exception;

public class SameIdUseException extends RuntimeException {
    public SameIdUseException(String message) {
        super(message);
    }
}
