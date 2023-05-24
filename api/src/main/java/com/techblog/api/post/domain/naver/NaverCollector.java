package com.techblog.api.post.domain.naver;

import com.techblog.api.post.domain.Collector;
import com.techblog.api.post.model.naver.NaverPost;
import com.techblog.common.constant.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class NaverCollector<T> implements Collector<T> {

    @Override
    public T toPostInfo(String url) {
        log.info("[NaverCollector] toPostInfo method is started");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        ResponseEntity<NaverPost> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET,
                entity,
                NaverPost.class);

        NaverPost naverPost = NaverPost.builder()
                .naverLinks(responseEntity.getBody().getNaverLinks())
                .naverContents(responseEntity.getBody().getNaverContents())
                .naverPages(responseEntity.getBody().getNaverPages())
                .build();

        return (T) naverPost;
    }

    @Override
    public void savePost(T postInfo) {
        log.info("[NaverCollector] savePost method is started");
        NaverPost naverPost = (NaverPost) postInfo;
        log.info("[NaverCollector] naverPost : {}", naverPost.getNaverContents().get(0));
    }

    @Override
    public Company getCompany() {
        return Company.NAVER;
    }
}