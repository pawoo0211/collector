package com.techblog.api.post.out;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CollectPostOut {

    private Integer totalCount;
    private String executedTime;

    @Builder
    public CollectPostOut(Integer totalCount, String executedTime) {
        this.totalCount = totalCount;
        this.executedTime = executedTime;
    }
}