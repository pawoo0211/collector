package com.techblog.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    COLLECT_SUCCESS(00, "글 수집 성공"),
    SAVE_URL_SUCCESS(20, "URL 저장 성공");

    private final int ResultCode;
    private final String resultMessage;

}
