package com.techblog.api.post.in;

import com.techblog.api.post.model.CompanyUrl;
import lombok.Getter;

import java.util.List;

@Getter
public class SaveUrlIn {

    private List<CompanyUrl> urlList;

}