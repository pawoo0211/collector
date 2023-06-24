package com.techblog.api.post.model;

import lombok.Builder;

public class CollectResult {

    private int savedPostCount;
    private Long executedTime;

    @Builder
    public CollectResult(int savedPostCount, long executedTime) {
        this.savedPostCount = savedPostCount;
        this.executedTime = executedTime;
    }

    public void setSavedPostCount(int savedPostCount) {
        this.savedPostCount = savedPostCount;
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