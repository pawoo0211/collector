package com.techblog.api.post.domain.naver;

import com.techblog.api.post.domain.Collector;
import com.techblog.api.post.model.naver.NaverPostInfo;
import com.techblog.common.constant.Company;
import com.techblog.dao.document.NaverPostEntity;
import com.techblog.dao.repository.NaverPostRepository;
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

    private final NaverPostRepository naverPostRepository;

    @Override
    public T toPostInfo(String url) {
        log.info("[NaverCollector] toPostInfo method is started");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        ResponseEntity<NaverPostInfo> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET,
                entity,
                NaverPostInfo.class);

        NaverPostInfo naverPostInfo = NaverPostInfo.builder()
                .naverLinks(responseEntity.getBody().getNaverLinks())
                .naverContents(responseEntity.getBody().getNaverContents())
                .naverPages(responseEntity.getBody().getNaverPages())
                .build();

        return (T) naverPostInfo;
    }

    @Override
    public void savePost(T postInfo) {
        log.info("[NaverCollector] savePost method is started");
        NaverPostInfo naverPostInfo = (NaverPostInfo) postInfo;
        NaverPostEntity naverPost = NaverPostEntity.builder()
                .title(naverPostInfo.getNaverContents().get(0).getPostTitle())
                .href(naverPostInfo.getNaverLinks().get(0).getHref())
                .build();

        naverPostRepository.save(naverPost);
        log.info("[NaverCollector] savePost`s result : {}", naverPostRepository.findByPostId(naverPost.getPostId()));
    }

    @Override
    public Company getCompany() {
        return Company.NAVER;
    }
}