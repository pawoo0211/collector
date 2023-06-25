package com.techblog.common.exception.domain;

import com.techblog.common.constant.ErrorCode;
import com.techblog.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CompanyNameException extends CustomException {

    private static String MESSAGE = ErrorCode.INVALID_COMPANY_NAME.getMessage();
    private static String DESCRIPTION = ErrorCode.INVALID_COMPANY_NAME.getDescription();

    public CompanyNameException() {
        super(MESSAGE, DESCRIPTION);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}