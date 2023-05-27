package com.techblog.api.post.domain.naver;

import com.techblog.api.post.domain.Collector;
import com.techblog.api.post.model.PostInfo;
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
public class NaverCollector implements Collector {

    private final PostRepository postRepository;

    @Override
    public List<PostInfo> toPostInfo(Company company) {
        log.info("[NaverCollector] toPostInfo method is started");
        List<String> naverPostUrlList= company.getUrlList();
        List<PostInfo> externalNaverPostInfoList = new ArrayList<>();

        /**
         * TODO
         * - WebClient로 리팩터링하기
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

        return externalNaverPostInfoList;
    }

    @Override
    public void savePost(List<PostInfo> externalNaverPostInfoList) {
        log.info("[NaverCollector] savePost method is started");
        InternalNaverPostInfo internalNaverPostInfo = null;
        List<InternalContent> internalContentList;
        List<InternalContent> rightInternalContentList = null;

        for (PostInfo externalNaverPostInfo : externalNaverPostInfoList) {
            /**
             * TODO
             * - 해당 라인에서 타입 캐스팅이 진행 되는 상태 -> 수정 예정
             */
            internalNaverPostInfo = toInternalNaverPostInfo((ExternalNaverPostInfo) externalNaverPostInfo);
        }

        if (internalNaverPostInfo != null) {
            internalContentList = internalNaverPostInfo.getContent();

            if (internalContentList.size() > 0) {
                rightInternalContentList = SavePossibilityContent(internalContentList);
                saveRightContent(rightInternalContentList);
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

    private List<InternalContent> SavePossibilityContent(List<InternalContent> internalContentList) {
        List<InternalContent> rightInternalContentList = new ArrayList<>();
        InternalContent standardizedContent = internalContentList.get(0);
        long standardizedPostPublishedAt = standardizedContent.getPostPublishedAt();

        if (postRepository.countByCompanyName(Company.NAVER.getName()) == 0) {
            log.info("[NaverCollector] Total naver post count is 0");
            return internalContentList;
        }

        for (InternalContent internalContent : internalContentList) {
            if (internalContent.getPostPublishedAt() < standardizedPostPublishedAt) {
                continue;
            }

            rightInternalContentList.add(internalContent);
        }

        rightInternalContentList.remove(0);

        return rightInternalContentList;
    }

    private void saveRightContent(List<InternalContent> rightInternalContentList) {
        for (InternalContent rightContent : rightInternalContentList) {
            PostEntity naverPost = PostEntity.builder()
                    .companyName(Company.NAVER.getName())
                    .title(rightContent.getPostTitle())
                    .url(rightContent.getUrl())
                    .build();

            postRepository.save(naverPost);
            PostEntity foundNaverPost = postRepository.findByPostId(naverPost.getPostId());
            log.info("[NaverCollector] savePost`s result : {}", foundNaverPost.getTitle());
        }
    }
}