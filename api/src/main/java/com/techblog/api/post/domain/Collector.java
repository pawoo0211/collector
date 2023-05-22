package com.techblog.api.post.domain;

import com.techblog.common.constant.Company;
import org.springframework.stereotype.Component;

@Component
public interface Collector {

    public void savePost(Object data);
    public Company getCompany();
}
