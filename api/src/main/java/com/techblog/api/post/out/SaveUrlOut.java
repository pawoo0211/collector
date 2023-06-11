package com.techblog.api.post.out;

import lombok.Getter;

@Getter
public class SaveUrlOut {

    private int savedUrlCount;

    public SaveUrlOut(int savedUrlCount) {
        this.savedUrlCount = savedUrlCount;
    }
}