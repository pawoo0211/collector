package com.techblog.api.post.model.naver.external;

import com.techblog.api.post.model.PostInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ExternalNaverPostInfo implements PostInfo {

    private List<Link> links;
    private List<Content> content;
    private Page page;

    @Builder
    public ExternalNaverPostInfo(List<Link> links, List<Content> content, Page page) {
        this.links = links;
        this.content = content;
        this.page = page;
    }

    @Override
    public ExternalNaverPostInfo getPostInfo() {
        return this;
    }
}