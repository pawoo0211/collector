package com.techblog.dao.document;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "NAVER")
@Getter
public class NaverPostEntity {

    @Id
    private String postId;
    private String title;
    private String href;

    @Builder
    public NaverPostEntity(String title, String href) {
        this.title = title;
        this.href = href;
    }
}