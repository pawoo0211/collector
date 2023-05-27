package com.techblog.api.post.in;

import com.techblog.common.constant.Company;
import lombok.Getter;

@Getter
public class CollectPostIn {

    private Company company;
    private String url;

}