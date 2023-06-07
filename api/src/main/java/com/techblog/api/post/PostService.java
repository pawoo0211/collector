package com.techblog.api.post;

import com.techblog.api.post.domain.CollectManager;
import com.techblog.api.post.in.CollectPostIn;
import com.techblog.api.post.model.CollectResult;
import com.techblog.api.post.model.Search;
import com.techblog.api.post.out.CollectPostOut;
import com.techblog.api.post.out.SearchPostOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final CollectManager collectManager;

    public CollectPostOut collectPost(CollectPostIn collectPostIn) {
        CollectResult collectResult = collectManager.collect(collectPostIn);

        return CollectPostOut.builder()
                .totalCount(collectResult.getSavedPostCount())
                .executedTime(collectResult.getExecutedTime().toString() + "ms")
                .build();
    }

    public SearchPostOut search(String keyword) {
        List<Search> searchList = new ArrayList<>();
        Search search = Search
                .builder()
                .title("네이버 기술 블로그 글")
                .link("www.test.com")
                .build();

        searchList.add(search);

        SearchPostOut searchPostOut = new SearchPostOut();
        searchPostOut.setSearchList(searchList);

        return searchPostOut;
    }
}