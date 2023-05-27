package com.techblog.api.post.domain.naver;

import com.techblog.api.post.domain.Collector;
import com.techblog.api.post.model.naver.external.Content;
import com.techblog.api.post.model.naver.internal.InternalContent;
import com.techblog.api.post.model.naver.internal.InternalNaverPostInfo;
import com.techblog.api.post.model.naver.external.ExternalNaverPostInfo;
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
        List<ExternalNaverPostInfo> externalNaverPostInfoList = new ArrayList<>();

        /**
         * TODO
         * WebClient로 리팩터링하기
         */
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        for (String url : naverPostUrlList) {
            ResponseEntity<ExternalNaverPostInfo> responseEntity = restTemplate.exchange(url,
                    HttpMethod.GET,
                    entity,
                    ExternalNaverPostInfo.class);

            ExternalNaverPostInfo externalNaverPostInfo = ExternalNaverPostInfo.builder()
                    .links(responseEntity.getBody().getLinks())
                    .content(responseEntity.getBody().getContent())
                    .page(responseEntity.getBody().getPage())
                    .build();

            externalNaverPostInfoList.add(externalNaverPostInfo);
        }

        /**
         * TODO
         * 여기서 제네릭 인터페이스를 만들어서 NaverPostInfo
         * 여기서 이렇게 제네릭으로 다시 타입 캐스팅 하는 것은 제네릭을 완전히 잘못 사용하고 있는 상태 -> 변경
         * */

        return (List<T>) externalNaverPostInfoList;
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
        List<ExternalNaverPostInfo> externalNaverPostInfoList = (List<ExternalNaverPostInfo>) postInfo;

        for (ExternalNaverPostInfo externalNaverPostInfo : externalNaverPostInfoList) {
            InternalNaverPostInfo internalNaverPostInfo = toInternalNaverPostInfo(externalNaverPostInfo);

            /**
             * TODO
             * - 발행일자를 체크해서 발행일자 이후의 글 들만 저장하기
             */
            for (InternalContent internalContent : internalNaverPostInfo.getContent()) {
                PostEntity naverPost = PostEntity.builder()
                        .title(internalContent.getPostTitle())
                        .url(internalContent.getUrl())
                        .build();

                postRepository.save(naverPost);
                PostEntity foundNaverPost = postRepository.findByPostId(naverPost.getPostId());
                log.info("[NaverCollector] savePost`s result : {}", foundNaverPost.getTitle());
            }
        }
    }

    @Override
    public Company getCompany() {
        return Company.NAVER;
    }

    private InternalNaverPostInfo toInternalNaverPostInfo(ExternalNaverPostInfo externalNaverPostInfo) {
        List<Content> externalContentList = externalNaverPostInfo.getContent();
        List<InternalContent> internalContentList = new ArrayList<>();

        for (Content externalContent : externalContentList) {
            InternalContent internalContent = InternalContent.builder()
                    .postTitle(externalContent.getPostTitle())
                    .postPublishedAt(externalContent.getPostPublishedAt())
                    .url(externalContent.getUrl())
                    .build();

            internalContentList.add(internalContent);
        }

        return InternalNaverPostInfo.builder()
                .content(internalContentList)
                .build();
    }
}