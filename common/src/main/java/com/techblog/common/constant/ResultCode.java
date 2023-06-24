package com.techblog.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    COLLECT_SUCCESS("COLLECT_SUCCESS", "글 수집 성공"),
    SAVE_URL_SUCCESS("SAVE_URL_SUCCESS", "URL 저장 성공");

    private final String message;
    private final String description;

}
