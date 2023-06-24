package com.techblog.api.post.domain;

import com.techblog.api.post.model.CollectResult;
import com.techblog.api.post.model.Post;
import com.techblog.common.constant.Company;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public interface Collector {

    public List<Post> toPost(Company company);
    public CompletableFuture<CollectResult> savePost(List<Post> postList);
    public Company getCompany();

}