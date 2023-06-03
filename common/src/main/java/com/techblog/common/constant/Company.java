package com.techblog.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Company {

    NAVER("NAVER", Arrays.asList(
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=0&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=1&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=2&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=3&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=4&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=5&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=6&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=7&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=8&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=9&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=10&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=11&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=12&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=13&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=14&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=15&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=16&size=20",
            "https://d2.naver.com/api/v1/contents?categoryId=2&page=17&size=20"
            ));

    private final String name;
    private final List<String> urlList;

}