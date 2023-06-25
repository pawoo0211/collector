package com.techblog.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomException extends RuntimeException{

    private String message;
    private String description;

    public CustomException(String message, String description) {
        this.message = message;
        this.description = description;
    }

    public abstract HttpStatus getStatus();
}