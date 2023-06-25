package com.techblog.api.post.model.nhn.internal;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InternalNhnContent {

    private int postId;
    private String url;
    private String title;
    private String contentPreview;
    private String regTime;
    private String publishTime;

    @Builder
    public InternalNhnContent(int postId, String url, String title, String contentPreview, String regTime,
                              String publishTime) {
        this.postId = postId;
        this.url = url;
        this.title = title;
        this.contentPreview = contentPreview;
        this.regTime = regTime;
        this.publishTime = publishTime;
    }
}