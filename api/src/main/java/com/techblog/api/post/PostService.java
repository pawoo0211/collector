package com.techblog.api.post;

import com.techblog.api.post.domain.CollectManager;
import com.techblog.api.post.in.CollectPostIn;
import com.techblog.api.post.in.SaveUrlIn;
import com.techblog.api.post.model.CollectResult;
import com.techblog.api.post.model.CompanyUrl;
import com.techblog.api.post.model.Search;
import com.techblog.api.post.out.CollectPostOut;
import com.techblog.api.post.out.SaveUrlOut;
import com.techblog.api.post.out.SearchPostOut;
import com.techblog.dao.jpa.CompanyUrlJpaEntity;
import com.techblog.dao.jpa.CompanyUrlJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final CollectManager collectManager;
    private final CompanyUrlJpaRepository companyUrlJpaRepository;

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

    @Transactional
    public SaveUrlOut saveUrl(SaveUrlIn saveUrlIn) {
        List<CompanyUrl> companyUrlList = saveUrlIn.getUrlList();
        int count = 0;

        for (CompanyUrl companyUrl : companyUrlList) {
            String companyName = companyUrl.getCompanyName();
            List<String> urlListIn = companyUrl.getUrlList();
            List<String> validatedUrlList = extractValidatedUrl(urlListIn);

            for (String url : validatedUrlList) {
                CompanyUrlJpaEntity companyUrlEntity = CompanyUrlJpaEntity.builder()
                        .url(url)
                        .companyName(companyName)
                        .build();

                companyUrlJpaRepository.save(companyUrlEntity);

                count += 1;
            }
        }

        SaveUrlOut saveUrlOut = new SaveUrlOut(count);
        return saveUrlOut;
    }

    private List<String> extractValidatedUrl(List<String> urlInList) {
        List<String> validatedUrlList = new ArrayList<>();

        for (String url : urlInList) {
            if (companyUrlJpaRepository.existsByUrl(url)) continue;
            validatedUrlList.add(url);
        }

        return validatedUrlList;
    }
}