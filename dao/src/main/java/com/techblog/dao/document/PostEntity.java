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
    private String title;
    private String url;

    @Builder
    public PostEntity(String title, String url) {
        this.title = title;
        this.url = url;
    }
}