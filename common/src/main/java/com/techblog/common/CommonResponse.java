package com.techblog.common;

import lombok.Getter;

@Getter
public class CommonResponse<T> {
    private final Boolean isSuccess;
    private final T out;

    private CommonResponse(Boolean isSuccess, T out) {
        this.isSuccess = isSuccess;
        this.out = out;
    }

    public static <T> CommonResponse ok(T out) {
        return new CommonResponse(true, out);
    }
}