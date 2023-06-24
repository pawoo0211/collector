package com.techblog.common;

import lombok.Getter;

@Getter
public class CommonResponse<T> {

    private final Boolean isSuccess;
    private String message;
    private String description;
    private final T out;

    private CommonResponse(Boolean isSuccess, String message, String description, T out) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.description = description;
        this.out = out;
    }

    public static <T> CommonResponse ok(String message, String description, T out) {
        return new CommonResponse(true, message, description, out);
    }
}