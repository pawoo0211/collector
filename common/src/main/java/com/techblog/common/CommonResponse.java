package com.techblog.common;

import lombok.Getter;

@Getter
public class CommonResponse<T> {

    private final Boolean isSuccess;
    private Integer resultCode;
    private String resultMessage;
    private final T out;

    private CommonResponse(Boolean isSuccess, Integer resultCode, String resultMessage, T out) {
        this.isSuccess = isSuccess;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.out = out;
    }

    public static <T> CommonResponse ok(Integer resultCode, String resultMessage, T out) {
        return new CommonResponse(true, resultCode, resultMessage, out);
    }
}