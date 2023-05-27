package com.techblog.api.post.domain;

import com.techblog.common.constant.Company;
import org.springframework.stereotype.Component;

@Component
public interface Collector<T> {

    public T toPostInfo(String url);
    public void savePost(T postInfo);
    public Company getCompany();

}