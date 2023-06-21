package com.techblog.api.post.domain.naver;

import com.techblog.api.post.domain.Collector;
import com.techblog.api.post.model.CollectResult;
import com.techblog.api.post.model.Post;
import com.techblog.api.post.model.naver.external.ExternalNaverContent;
import com.techblog.api.post.model.naver.internal.InternalContent;
import com.techblog.api.post.model.naver.internal.InternalNaverPost;
import com.techblog.api.post.model.naver.external.ExternalNaverPost;
import com.techblog.common.constant.Company;
import com.techblog.common.domain.webclient.ApiConnector;
import com.techblog.dao.jpa.CompanyUrlJpaEntity;
import com.techblog.dao.jpa.CompanyUrlJpaRepository;
import com.techblog.dao.mongodb.PostMongoEntity;
import com.techblog.dao.mongodb.PostMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class NaverCollector implements Collector {

    private final PostMongoRepository postMongoRepository;
    private final ApiConnector apiConnector;
    private final CompanyUrlJpaRepository companyUrlJpaRepository;

    @Override
    public List<Post> toPost(Company company) {
        List<CompanyUrlJpaEntity> naverUrlJpaEntityList = companyUrlJpaRepository
                .findAllByCompanyName(company.getName());

        List<String> naverPostUrlList = naverUrlJpaEntityList.stream()
                .map(CompanyUrlJpaEntity::getUrl)
                .collect(Collectors.toList());

        List<Post> externalNaverPostList = new ArrayList<>();

        log.info("[NaverCollector] Data communication is started");
        for (String url : naverPostUrlList) {
            ExternalNaverPost externalNaverPost = apiConnector.getHttpCall(url, ExternalNaverPost.class);
            externalNaverPostList.add(externalNaverPost);
        }
        log.info("[NaverCollector] Data communication is end");

        return externalNaverPostList;
    }

    @Override
    public CollectResult savePost(List<Post> externalNaverPostList) {
        log.info("[NaverCollector] savePost method is started");
        List<InternalNaverPost> internalNaverPostList = new ArrayList<>();
        List<InternalContent> internalContentList;
        List<InternalContent> rightInternalContentList;
        int savedPostCount = 0;

        for (Post externalNaverPost : externalNaverPostList) {
            InternalNaverPost internalNaverPost = toInternalNaverPost(externalNaverPost);
            internalNaverPostList.add(internalNaverPost);
        }

        for (InternalNaverPost internalNaverPost : internalNaverPostList) {
            if (postMongoRepository.countByCompanyName(Company.NAVER.getName()) == 0) {
                log.info("[NaverCollector] Total naver post count is 0");
                for (InternalNaverPost naverPostInfo : internalNaverPostList) {
                    savedPostCount += saveRightContent(naverPostInfo.getContent());
                }
                break;
            } else {
                internalContentList = internalNaverPost.getContent();
                rightInternalContentList = savePossibilityContent(internalContentList);

                savedPostCount += saveRightContent(rightInternalContentList);
            }
        }
        log.info("[NaverCollector] savePost method is end");

        return CollectResult.builder()
                .savedPostCount(savedPostCount)
                .executedTime(0L)
                .build();
    }

    @Override
    public Company getCompany() {
        return Company.NAVER;
    }

    private <T extends Post> InternalNaverPost toInternalNaverPost(T externalNaverPost) {
        List<ExternalNaverContent> externalExternalNaverContentList = externalNaverPost.getContent();
        List<InternalContent> internalContentList = new ArrayList<>();

        for (ExternalNaverContent externalExternalNaverContent : externalExternalNaverContentList) {
            String contentPreview = externalExternalNaverContent.getPostHtml().substring(29);

            InternalContent internalContent = InternalContent.builder()
                    .postTitle(externalExternalNaverContent.getPostTitle())
                    .contentPreview(contentPreview)
                    .postPublishedAt(externalExternalNaverContent.getPostPublishedAt())
                    .url(externalExternalNaverContent.getUrl())
                    .build();

            internalContentList.add(internalContent);
        }

        return InternalNaverPost.builder()
                .content(internalContentList)
                .build();
    }

    private List<InternalContent> savePossibilityContent(List<InternalContent> internalContentList) {
        List<InternalContent> rightInternalContentList = new ArrayList<>();
        InternalContent standardizedContent = internalContentList.get(0);
        long standardizedPostPublishedAt = standardizedContent.getPostPublishedAt();

        for (InternalContent internalContent : internalContentList) {
            if (internalContent.getPostPublishedAt() < standardizedPostPublishedAt) {
                continue;
            }
            rightInternalContentList.add(internalContent);
        }

        rightInternalContentList.remove(0);

        return rightInternalContentList;
    }

    private int saveRightContent(List<InternalContent> rightInternalContentList) {
        int savedPostCount = 0;

        for (InternalContent rightContent : rightInternalContentList) {

            PostMongoEntity naverPost = PostMongoEntity.builder()
                    .companyName(Company.NAVER.getName())
                    .title(rightContent.getPostTitle())
                    .contentPreview(rightContent.getContentPreview())
                    .url(rightContent.getUrl())
                    .build();

            postMongoRepository.save(naverPost);
            savedPostCount += 1;
            PostMongoEntity foundNaverPost = postMongoRepository.findByPostId(naverPost.getPostId());
            log.info("[NaverCollector] savePost`s result : {}", foundNaverPost.getTitle());
        }

        return savedPostCount;
    }
}