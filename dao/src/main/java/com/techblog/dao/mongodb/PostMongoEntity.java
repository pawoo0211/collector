package com.techblog.dao.mongodb;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "post")
@Getter
public class PostMongoEntity {

    @Id
    private String postId;
    private String companyName;
    private String title;
    private String contentPreview;
    private String url;

    @Builder
    public PostMongoEntity(String companyName, String title, String contentPreview, String url) {
        this.companyName = companyName;
        this.title = title;
        this.contentPreview = contentPreview;
        this.url = url;
    }
}