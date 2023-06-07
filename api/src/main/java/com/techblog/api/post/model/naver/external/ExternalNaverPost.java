package com.techblog.api.post.model.naver.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techblog.api.post.model.PostInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ExternalNaverPost extends PostInfo {

    private List<ExternalNaverLink> links;
    private List<ExternalNaverContent> content;
    private ExternalNaverPage page;

    @Builder
    public ExternalNaverPost(List<ExternalNaverLink> links, List<ExternalNaverContent> content, ExternalNaverPage page) {
        this.links = links;
        this.content = content;
        this.page = page;
    }
}