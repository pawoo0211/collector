package com.techblog.api.post.in;

import com.techblog.common.constant.Company;
import lombok.Getter;

@Getter
public class CollectPostIn<T> {

    private Company company;
    private T data;

}