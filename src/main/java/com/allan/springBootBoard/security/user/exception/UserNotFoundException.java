package com.allan.springBootBoard.security.user.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotFoundException extends AuthenticationException {
    public UserNotFoundException(String id) {
        super(id + " NotFoundException");
    }
}
