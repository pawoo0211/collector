package com.techblog.api.post.domain.nhn;

import com.techblog.api.post.domain.Collector;
import com.techblog.api.post.model.CollectResult;
import com.techblog.api.post.model.Post;
import com.techblog.api.post.model.nhn.external.ExternalNhnPostPerLang;
import com.techblog.api.post.model.nhn.external.ExternalNhnPost;
import com.techblog.api.post.model.nhn.external.ExternalNhnPostVo;
import com.techblog.api.post.model.nhn.internal.InternalNhnContent;
import com.techblog.api.post.model.nhn.internal.InternalNhnPost;
import com.techblog.common.constant.Company;
import com.techblog.common.util.datetime.CustomDateTime;
import com.techblog.common.domain.webclient.ApiConnector;
import com.techblog.dao.jpa.CompanyUrlJpaEntity;
import com.techblog.dao.jpa.CompanyUrlJpaRepository;
import com.techblog.dao.mongodb.PostMongoEntity;
import com.techblog.dao.mongodb.PostMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class NhnCollector implements Collector {

    private final CompanyUrlJpaRepository companyUrlJpaRepository;
    private final ApiConnector apiConnector;
    private final PostMongoRepository postMongoRepository;
    private final String FIXED_NHN_URL = "https://meetup.nhncloud.com/posts/";

    @Override
    public List<Post> toPost(Company company) {
        List<CompanyUrlJpaEntity> nhnUrlJpaEntityList = companyUrlJpaRepository
                .findAllByCompanyName(company.getName());

        List<String> nhnPostUrlList = nhnUrlJpaEntityList.stream()
                .map(CompanyUrlJpaEntity::getUrl)
                .collect(Collectors.toList());

        List<Post> externalNhnPostList = new ArrayList<>();

        log.info("[NhnCollector] Data communication is started");
        for (String url : nhnPostUrlList) {
            ExternalNhnPost externalNhnPost = apiConnector.getHttpCall(url, ExternalNhnPost.class);
            externalNhnPostList.add(externalNhnPost);
        }
        log.info("[NhnCollector] Data communication is end");

        return externalNhnPostList;
    }

    @Override
    @Async("customThreadPoolExecutor")
    public CompletableFuture<CollectResult> savePost(List<Post> postList) {
        log.info("[NhnCollector] savePost method is started");
        List<InternalNhnPost> internalNhnPostList = new ArrayList<>();
        List<InternalNhnContent> rightNhnContentVoList;
        int savedPostCount = 0;

        for (Post externalNhnPost : postList) {
            InternalNhnPost internalNhnPost = toInternalNhnPost(externalNhnPost);
            internalNhnPostList.add(internalNhnPost);
        }

        for (InternalNhnPost internalNhnPost : internalNhnPostList) {
            if (postMongoRepository.countByCompanyName(Company.NHN.getName()) == 0) {
                log.info("[NaverCollector] Total nhn post count is 0");
                for (InternalNhnPost internalPostInfo : internalNhnPostList) {
                    savedPostCount += saveRightContent(internalPostInfo.getContent());
                }
                break;
            } else if (internalNhnPost.getContent().size() == 0) {
                continue;
            } else {
                rightNhnContentVoList = savePossibilityContent(internalNhnPost.getContent());
                savedPostCount += saveRightContent(rightNhnContentVoList);
            }
        }
        log.info("[NhnCollector] savePost method is end");

        CollectResult collectResult = CollectResult.builder()
                .savedPostCount(savedPostCount)
                .executedTime(0L)
                .build();

        return CompletableFuture.completedFuture(collectResult);
    }

    @Override
    public Company getCompany() {
        return Company.NHN;
    }

    private <T extends Post> InternalNhnPost toInternalNhnPost(T externalNhnPost) {
        List<ExternalNhnPostVo> content = externalNhnPost.getContent();
        List<InternalNhnContent> internalNhnContentList = new ArrayList<>();

        for (ExternalNhnPostVo externalNhnPostVo : content) {
            ExternalNhnPostPerLang externalNhnPostPerLang = externalNhnPostVo.getContent();
            int postId = externalNhnPostPerLang.getPostId();

            InternalNhnContent internalNhnContent = InternalNhnContent.builder()
                    .postId(postId)
                    .url(FIXED_NHN_URL + postId)
                    .title(externalNhnPostPerLang.getTitle())
                    .contentPreview(externalNhnPostVo.getContentPreview())
                    .regTime(externalNhnPostPerLang.getRegTime())
                    .publishTime(externalNhnPostVo.getPublishTime())
                    .build();

            internalNhnContentList.add(internalNhnContent);
        }

        return InternalNhnPost.builder()
                .content(internalNhnContentList)
                .build();
    }

    private List<InternalNhnContent> savePossibilityContent(List<InternalNhnContent> internalNhnContentList) {
        List<InternalNhnContent> rightInternalContentVoList = new ArrayList<>();
        InternalNhnContent standardInternalNhnContent = internalNhnContentList.get(0);
        final LocalDateTime STANDARD_REGISTER_TIME = CustomDateTime.toLocalDateTime(standardInternalNhnContent.getRegTime());

        for (InternalNhnContent internalNhnContent : internalNhnContentList) {
            LocalDateTime registerTime = CustomDateTime.toLocalDateTime(internalNhnContent.getRegTime());

            if (registerTime.isBefore(STANDARD_REGISTER_TIME)) {
                continue;
            }
            rightInternalContentVoList.add(internalNhnContent);
        }

        rightInternalContentVoList.remove(0);

        return rightInternalContentVoList;
    }

    private int saveRightContent(List<InternalNhnContent> internalNhnContent) {
        int savedPostCount = 0;

        for (InternalNhnContent rightContent : internalNhnContent) {
            PostMongoEntity nhnPost = PostMongoEntity.builder()
                    .companyName(Company.NHN.getName())
                    .title(rightContent.getTitle())
                    .contentPreview(rightContent.getContentPreview())
                    .url(rightContent.getUrl())
                    .build();

            postMongoRepository.save(nhnPost);
            savedPostCount += 1;
            PostMongoEntity foundNaverPost = postMongoRepository.findByPostId(nhnPost.getPostId());
            log.info("[NhnCollector] savePost`s result : {}", foundNaverPost.getTitle());
        }
        return savedPostCount;
    }
}