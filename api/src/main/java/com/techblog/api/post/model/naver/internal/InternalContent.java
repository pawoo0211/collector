package com.techblog.api.post.model.naver.internal;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InternalContent {

    private String postTitle;
    private long postPublishedAt;
    private String url;

    @Builder
    public InternalContent(String postTitle, long postPublishedAt, String url) {
        this.postTitle = postTitle;
        this.postPublishedAt = postPublishedAt;
        this.url = url;
    }
}