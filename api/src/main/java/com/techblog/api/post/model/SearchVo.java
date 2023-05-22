package com.techblog.api.post.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchVo {

    private String title;
    private String link;

    @Builder
    public SearchVo(String title, String link) {
        this.title = title;
        this.link = link;
    }
}