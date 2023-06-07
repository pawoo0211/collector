package com.techblog.api.post.model.nhn.internal;

import com.techblog.api.post.model.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class InternalNhnPost extends Post {

    private List<InternalNhnContent> content;

    @Builder
    public InternalNhnPost(List<InternalNhnContent> content) {
        this.content = content;
    }
}