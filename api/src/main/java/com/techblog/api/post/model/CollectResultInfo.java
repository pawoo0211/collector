package com.techblog.api.post.model;

import lombok.Builder;

public class CollectResultInfo {

    private int savedPostCount;
    private Long executedTime;

    @Builder
    public CollectResultInfo(int savedPostCount, long executedTime) {
        this.savedPostCount = savedPostCount;
        this.executedTime = executedTime;
    }

    public void setExecutedTime(long executedTime) {
        this.executedTime = executedTime;
    }

    public int getSavedPostCount() {
        return savedPostCount;
    }

    public Long getExecutedTime() {
        return executedTime;
    }
}