package com.techblog.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum Company {

    NAVER("NAVER", Arrays.asList("https://d2.naver.com/api/v1/contents?categoryId=2&page=0&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=1&size=20"));

    private final String name;
    private final List<String> urlList;

}