package com.techblog.api.post.out;

import com.techblog.api.post.model.SearchVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchPostOut {

    private List<SearchVo> searchVoList;

}