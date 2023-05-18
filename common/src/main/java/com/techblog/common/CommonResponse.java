package com.techblog.common;

import lombok.Getter;

@Getter
public class CommonResponse<T> {

    private final Boolean isSuccess;
    private Integer resultCode;
    private Integer totalCount;
    private final T out;

    private CommonResponse(Boolean isSuccess, Integer resultCode, Integer totalCount, T out) {
        this.isSuccess = isSuccess;
        this.resultCode = resultCode;
        this.totalCount = totalCount;
        this.out = out;
    }

    public static <T> CommonResponse ok(Integer totalCount,  T out) {
        return new CommonResponse(true, 00, totalCount, out);
    }
}