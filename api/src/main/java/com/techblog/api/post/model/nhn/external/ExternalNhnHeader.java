package com.techblog.api.post.model.nhn.external;

import lombok.Getter;

@Getter
public class ExternalNhnHeader {

    private boolean isSuccessful;
    private int resultCode;
    private String resultMessage;
}