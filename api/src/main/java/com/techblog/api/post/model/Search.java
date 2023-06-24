package com.techblog.api.post.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Search {

    private String title;
    private String link;

    @Builder
    public Search(String title, String link) {
        this.title = title;
        this.link = link;
    }
}