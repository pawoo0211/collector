package com.techblog.api.post.model.nhn.external;

import lombok.Getter;

@Getter
public class ExternalNhnHeaderVo {

    private boolean isSuccessful;
    private int resultCode;
    private String resultMessage;

}