package com.techblog.api.post.domain.naver;

import com.techblog.api.post.domain.Collector;
import com.techblog.api.post.model.naver.NaverPostInfo;
import com.techblog.common.constant.Company;
import com.techblog.dao.document.PostEntity;
import com.techblog.dao.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j

/**
 * TODO
 * NaverCollecotr implements Collector<NaverPostInfo>
 */

public class NaverCollector<T> implements Collector<T> {

    private final PostRepository postRepository;

    @Override
    public List<T> toPostInfo(Company company) {
        log.info("[NaverCollector] toPostInfo method is started");
        List<String> naverPostUrlList= company.getUrlList();
        List<NaverPostInfo> naverPostInfoList = new ArrayList<>();

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

        for (String url : naverPostUrlList ) {
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

            naverPostInfoList.add(naverPostInfo);
        }

        /**
         * TODO
         * 여기서 제네릭 인터페이스를 만들어서 NaverPostInfo
         * 여기서 이렇게 제네릭으로 다시 타입 캐스팅 하는 것은 제네릭을 완전히 잘못 사용하고 있는 상태 -> 변경
         * */

        return (List<T>) naverPostInfoList;
    }

    @Override
    public void savePost(List<T> postInfo) {
        log.info("[NaverCollector] savePost method is started");
        /**
         * TODO
         * 제네릭을 사용할 때 아래와 같이 타입 캐스팅을 하면 안됨
         * NaverPostInfo -> 내부적으로 사용하는 NaverPostInfo 객체
         * NaverPostInfoResponse -> 외부와 통신해서 받은 NaverPostInfo 객체
         */
        List<NaverPostInfo> naverPostInfoList = (List<NaverPostInfo>) postInfo;

        for (NaverPostInfo naverPostInfo : naverPostInfoList) {

            /**
             * TODO
             * GET을 연달아 호출하는 것은 좋은 방식이 아님 -> 변경
             */
            PostEntity naverPost = PostEntity.builder()
                    .title(naverPostInfo.getNaverContents().get(0).getPostTitle())
                    .href(naverPostInfo.getNaverLinks().get(0).getHref())
                    .build();

            /**
             * TODO
             * 1. 발행일자를 체크해서 발행일자 이후의 글 들만 저장하기
             */
            postRepository.save(naverPost);
            log.info("[NaverCollector] savePost`s result : {}", postRepository.findByPostId(naverPost.getPostId()));
        }
    }

    @Override
    public Company getCompany() {
        return Company.NAVER;
    }
}