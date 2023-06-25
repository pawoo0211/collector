package com.techblog.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity customExceptionHandler(CustomException e) {
        e.printStackTrace();
        log.info("[ExceptionController] Error message : {}", e.getMessage());

        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(), e.getDescription());

        return ResponseEntity.status(e.getStatus())
                .body(exceptionResponse);
    }
}