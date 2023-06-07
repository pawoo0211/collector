package com.techblog.api.post.model.nhn.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techblog.api.post.model.PostInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ExternalNhnPost extends PostInfo {

    private ExternalNhnHeader header;
    private int totalCount;

    @JsonProperty(value = "posts")
    private List<ExternalNhnPostVo> content;

}