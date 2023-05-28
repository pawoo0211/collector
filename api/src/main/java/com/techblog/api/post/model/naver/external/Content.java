package com.techblog.api.post.model.naver.external;

import lombok.Getter;
import java.util.List;

@Getter
public class Content {

    private String postTitle;
    private String postImage;
    private String postHtml;
    private long postPublishedAt;
    private String url;
    private Integer viewCount;
    private String author;
    private List<String> links;

}