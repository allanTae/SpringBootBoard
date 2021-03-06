package com.allan.springBootBoard.web.error.code;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),


    // Member
    Member_ID_DUPLICATION(400, "M001", "MemberId is Duplication"),
    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),
    MEMBER_INPUT_NULL_VALUE(400, "M003", "inputValue is null"),


    // Database
    INTEGRITY_VIOLATION(400, "D001", "Data's Itegrity is violation");


    private final String code;      // error code
    private final String message;   // error message
    private int status;             // http status code

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }


}