package com.techblog.api.post;

import com.techblog.api.post.in.CollectPostIn;
import com.techblog.api.post.model.SearchVo;
import com.techblog.api.post.out.CollectPostOut;
import com.techblog.api.post.out.SearchPostOut;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    public CollectPostOut collectPost(CollectPostIn collectPostIn) {

        return CollectPostOut.builder()
                .checkUpdate(Boolean.FALSE)
                .totalCount(100)
                .executedTime(10000L)
                .build();
    }

    public SearchPostOut search(String keyword) {
        List<SearchVo> searchVoList = new ArrayList<>();
        SearchVo searchVo = SearchVo
                .builder()
                .title("네이버 기술 블로그 글")
                .link("www.test.com")
                .build();

        searchVoList.add(searchVo);

        SearchPostOut searchPostOut = new SearchPostOut();
        searchPostOut.setSearchVoList(searchVoList);

        return searchPostOut;
    }
}