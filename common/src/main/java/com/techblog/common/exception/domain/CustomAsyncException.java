package com.techblog.common.exception.domain;

import com.techblog.common.constant.ErrorCode;
import com.techblog.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CustomAsyncException extends CustomException {

    private static String MESSAGE = ErrorCode.ASYNC_EXCEPTION.getMessage();
    private static String DESCRIPTION = ErrorCode.ASYNC_EXCEPTION.getDescription();

    public CustomAsyncException() {
        super(MESSAGE, DESCRIPTION);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}