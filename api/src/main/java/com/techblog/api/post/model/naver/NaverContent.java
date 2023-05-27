package com.techblog.api.post.model.naver;

import lombok.Getter;
import java.util.List;

@Getter
public class NaverContent {

    private String postTitle;
    private String postImage;
    private String postHtml;
    private Long postPublishedAt;
    private String url;
    private Integer viewCount;
    private String author;
    private List<String> links;

}