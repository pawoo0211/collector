package com.techblog.api.post.model.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NaverPostInfo {

    @JsonProperty(value = "links")
    private List<NaverLinks> naverLinks;

    @JsonProperty(value = "content")
    private List<NaverContent> naverContents;

    @JsonProperty(value = "page")
    private NaverPage naverPages;

    @Builder
    public NaverPostInfo(List<NaverLinks> naverLinks, List<NaverContent> naverContents, NaverPage naverPages) {
        this.naverLinks = naverLinks;
        this.naverContents = naverContents;
        this.naverPages = naverPages;
    }
}