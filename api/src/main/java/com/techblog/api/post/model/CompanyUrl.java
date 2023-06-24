package com.techblog.api.post.model;

import lombok.Getter;

import java.util.List;

@Getter
public class CompanyUrl {

    private String companyName;
    private List<String> urlList;

}