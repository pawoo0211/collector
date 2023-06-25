package com.techblog.common.exception;

import lombok.Getter;

@Getter
public class ExceptionResponse {

    private String message;
    private String description;

    public ExceptionResponse(String message, String description) {
        this.message = message;
        this.description = description;
    }
}