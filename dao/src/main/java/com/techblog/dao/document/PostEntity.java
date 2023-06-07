package com.techblog.dao.document;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "post")
@Getter
public class PostEntity {

    @Id
    private String postId;
    private String companyName;
    private String title;
    private String contentPreview;
    private String url;

    @Builder
    public PostEntity(String companyName, String title, String contentPreview, String url) {
        this.companyName = companyName;
        this.title = title;
        this.contentPreview = contentPreview;
        this.url = url;
    }
}