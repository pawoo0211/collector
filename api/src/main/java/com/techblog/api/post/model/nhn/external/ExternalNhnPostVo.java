package com.techblog.api.post.model.nhn.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ExternalNhnPostVo {

    private String publishTime;
    private String publishStatus;
    private String contentStatus;

    @JsonProperty(value = "postPerLang")
    private ExternalNhnPostPerLang content;

    private String contentPreview;

}