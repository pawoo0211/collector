package com.techblog.api.post.in;

import com.techblog.common.constant.Company;
import lombok.Getter;

@Getter
public class CollectPostIn<T> {

    public Company company;
    public T data;

}