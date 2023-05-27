package com.techblog.api.post.model.naver.external;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ExternalNaverPostInfo {

    private List<Link> links;
    private List<Content> content;
    private Page page;

    @Builder
    public ExternalNaverPostInfo(List<Link> links, List<Content> content, Page page) {
        this.links = links;
        this.content = content;
        this.page = page;
    }
}