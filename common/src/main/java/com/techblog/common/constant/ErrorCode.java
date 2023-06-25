package com.techblog.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ASYNC_EXCEPTION("INTERNAL_SERVER_ERROR", "내부적인 서버 에러가 발생했습니다.");

    private final String message;
    private final String description;
}