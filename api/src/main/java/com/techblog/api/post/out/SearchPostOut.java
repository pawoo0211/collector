package com.techblog.api.post.out;

import com.techblog.api.post.model.Search;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchPostOut {

    private List<Search> searchList;

}