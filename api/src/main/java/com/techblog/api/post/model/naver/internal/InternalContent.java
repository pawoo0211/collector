package com.techblog.api.post.model.naver.internal;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InternalContent {

    private String postTitle;
    private String contentPreview;
    private long postPublishedAt;
    private String url;

    @Builder
    public InternalContent(String postTitle, String contentPreview, long postPublishedAt, String url) {
        this.postTitle = postTitle;
        this.contentPreview = contentPreview;
        this.postPublishedAt = postPublishedAt;
        this.url = url;
    }
}