package com.techblog.common.exception;

public class ExceptionResponse {

    private String message;
    private String description;

    public ExceptionResponse(String message, String description) {
        this.message = message;
        this.description = description;
    }
}