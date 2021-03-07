package com.allan.springBootBoard.web.exception;

public class SameIdUseException extends RuntimeException {
    public SameIdUseException(String message) {
        super(message);
    }
}
