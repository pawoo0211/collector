package com.techblog.api.post.model.naver.internal;

import com.techblog.api.post.model.PostInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class InternalNaverPostInfo implements PostInfo {

    private List<InternalContent> content;

    @Builder
    public InternalNaverPostInfo(List<InternalContent> content) {
        this.content = content;
    }

}