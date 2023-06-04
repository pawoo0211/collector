package com.techblog.api.post.model.nhn.internal;

import com.techblog.api.post.model.PostInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class InternalNhnPostVo extends PostInfo {

    private List<InternalNhnContentVo> content;

    @Builder
    public InternalNhnPostVo(List<InternalNhnContentVo> content) {
        this.content = content;
    }
}