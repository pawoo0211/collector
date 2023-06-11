package com.techblog.api.post.model.naver.internal;

import com.techblog.api.post.model.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class InternalNaverPost extends Post {

    private List<InternalContent> content;

    @Builder
    public InternalNaverPost(List<InternalContent> content) {
        this.content = content;
    }

}