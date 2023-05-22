package com.techblog.api.post.out;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CollectPostOut {

    private Boolean checkUpdate;
    private Integer totalCount;
    private Long executedTime;

    @Builder
    public CollectPostOut(Boolean checkUpdate, Integer totalCount, Long executedTime) {
        this.checkUpdate = checkUpdate;
        this.totalCount = totalCount;
        this.executedTime = executedTime;
    }
}