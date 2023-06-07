package com.techblog.api.post.model.nhn.internal;

import com.techblog.api.post.model.PostInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class InternalNhnPost extends PostInfo {

    private List<InternalNhnContent> content;

    @Builder
    public InternalNhnPost(List<InternalNhnContent> content) {
        this.content = content;
    }
}