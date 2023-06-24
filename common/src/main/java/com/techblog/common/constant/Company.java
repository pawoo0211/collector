package com.techblog.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Company {

    NAVER("NAVER"),
    NHN("NHN");

    private final String name;

}