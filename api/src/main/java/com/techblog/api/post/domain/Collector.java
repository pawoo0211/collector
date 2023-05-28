package com.techblog.api.post.domain;

import com.techblog.api.post.model.PostInfo;
import com.techblog.common.constant.Company;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface Collector {

    public List<PostInfo> toPostInfo(Company company);
    public void savePost(List<PostInfo> postInfoList);
    public Company getCompany();

}