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

/**
 * TODO
 * NaverCollecotr implements Collector<NaverPostInfo>
 */

public class NaverCollector<T> implements Collector<T> {

    private final NaverPostRepository naverPostRepository;

    @Override
    public T toPostInfo(String url) {
        log.info("[NaverCollector] toPostInfo method is started");

        /**
         * TODO
         * WebClient로 리팩터링하기
         */

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        /**
         * TODO
         * 1. 응답 받는 것 중에 필요 없는 것들(NULL)인 것들은 없애기
         * EX) links ...
         * 2. 응답 받는 것을 List로 감싸고 for문 돌면서 저장하기
         */

        ResponseEntity<NaverPostInfo> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET,
                entity,
                NaverPostInfo.class);

        /**
         * TODO
         * naverPostInfo를 두개로 분리하기
         * 1. 네이버에서 응답 받은 값 = NaverPostInfoResponse
         * 2. 네이버에서 응답 받은 값을 내부에서 사용하는 DTO = NaverPostInfo
         * -> 즉 객체를 분리해서 네이버에 종속 되는 것을 막기
         *
         */
        NaverPostInfo naverPostInfo = NaverPostInfo.builder()
                .naverLinks(responseEntity.getBody().getNaverLinks())
                .naverContents(responseEntity.getBody().getNaverContents())
                .naverPages(responseEntity.getBody().getNaverPages())
                .build();

        /**
         * TODO
         * 여기서 제네릭 인터페이스를 만들어서 NaverPostInfo
         * */
        return (T) naverPostInfo;
    }

    @Override
    public void savePost(T postInfo) {
        log.info("[NaverCollector] savePost method is started");

        /**
         * TODO
         * 제네릭을 사용할 때 아래와 같이 타입 캐스팅을 하면 안됨
         */
        NaverPostInfo naverPostInfo = (NaverPostInfo) postInfo;

        /**
         * TODO
         * GET을 연달아 호출하는 것은 좋은 방식이 아님 -> 변경
         */
        NaverPostEntity naverPost = NaverPostEntity.builder()
                .title(naverPostInfo.getNaverContents().get(0).getPostTitle())
                .href(naverPostInfo.getNaverLinks().get(0).getHref())
                .build();

        /**
         * TODO
         * 1. 발행일자를 체크해서 발행일자 이후의 글 들만 저장하기
         */

        naverPostRepository.save(naverPost);
        log.info("[NaverCollector] savePost`s result : {}", naverPostRepository.findByPostId(naverPost.getPostId()));
    }

    @Override
    public Company getCompany() {
        return Company.NAVER;
    }
}