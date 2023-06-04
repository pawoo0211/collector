package com.techblog.api.post.model.nhn.internal;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InternalNhnContentVo {

    private int postId;
    private String url;
    private String title;
    private String contentPreview;
    private String publishTime;

    @Builder
    public InternalNhnContentVo(int postId, String url, String title, String contentPreview, String publishTime) {
        this.postId = postId;
        this.url = url;
        this.title = title;
        this.contentPreview = contentPreview;
        this.publishTime = publishTime;
    }
}