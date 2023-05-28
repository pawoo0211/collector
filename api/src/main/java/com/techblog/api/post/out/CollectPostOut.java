package com.techblog.api.post.out;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CollectPostOut {

    private Integer totalCount;
    private Long executedTime;

    @Builder
    public CollectPostOut(Integer totalCount, Long executedTime) {
        this.totalCount = totalCount;
        this.executedTime = executedTime;
    }
}