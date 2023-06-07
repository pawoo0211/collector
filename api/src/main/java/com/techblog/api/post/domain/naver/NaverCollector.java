package com.techblog.api.post.domain.naver;

import com.techblog.api.post.domain.Collector;
import com.techblog.api.post.model.CollectResultInfo;
import com.techblog.api.post.model.PostInfo;
import com.techblog.api.post.model.naver.external.ExternalNaverContent;
import com.techblog.api.post.model.naver.internal.InternalContent;
import com.techblog.api.post.model.naver.internal.InternalNaverPostVo;
import com.techblog.api.post.model.naver.external.ExternalNaverPost;
import com.techblog.common.constant.Company;
import com.techblog.common.webclient.DataCommunication;
import com.techblog.dao.document.PostEntity;
import com.techblog.dao.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NaverCollector implements Collector {

    private final PostRepository postRepository;
    private final DataCommunication dataCommunication;

    @Override
    public List<PostInfo> toPostInfo(Company company) {
        List<String> naverPostUrlList= company.getUrlList();
        List<PostInfo> externalNaverPostInfoList = new ArrayList<>();

        log.info("[NaverCollector] Data communication is started");
        for (String url : naverPostUrlList) {
            ExternalNaverPost externalNaverPost = dataCommunication.getHttpCall(url, ExternalNaverPost.class);
            externalNaverPostInfoList.add(externalNaverPost);
        }

        return externalNaverPostInfoList;
    }

    @Override
    public CollectResultInfo savePost(List<PostInfo> externalNaverPostInfoList) {
        log.info("[NaverCollector] savePost method is started");
        List<InternalNaverPostVo> internalNaverPostVoList = new ArrayList<>();
        List<InternalContent> internalContentList;
        List<InternalContent> rightInternalContentList;
        int savedPostCount = 0;

        for (PostInfo externalNaverPostInfo : externalNaverPostInfoList) {
            InternalNaverPostVo internalNaverPostVo = toInternalNaverPostInfo(externalNaverPostInfo);
            internalNaverPostVoList.add(internalNaverPostVo);
        }

        for (InternalNaverPostVo internalNaverPostVo : internalNaverPostVoList) {
            if (postRepository.countByCompanyName(Company.NAVER.getName()) == 0) {
                log.info("[NaverCollector] Total naver post count is 0");
                for (InternalNaverPostVo naverPostInfo : internalNaverPostVoList) {
                    savedPostCount += saveRightContent(naverPostInfo.getContent());
                }
                break;
            } else {
                internalContentList = internalNaverPostVo.getContent();
                rightInternalContentList = savePossibilityContent(internalContentList);

                savedPostCount += saveRightContent(rightInternalContentList);
            }
        }

        return CollectResultInfo.builder()
                .savedPostCount(savedPostCount)
                .executedTime(0L)
                .build();
    }

    @Override
    public Company getCompany() {
        return Company.NAVER;
    }

    private <T extends PostInfo> InternalNaverPostVo toInternalNaverPostInfo(T externalNaverPostInfo) {
        List<ExternalNaverContent> externalExternalNaverContentList = externalNaverPostInfo.getContent();
        List<InternalContent> internalContentList = new ArrayList<>();

        for (ExternalNaverContent externalExternalNaverContent : externalExternalNaverContentList) {
            InternalContent internalContent = InternalContent.builder()
                    .postTitle(externalExternalNaverContent.getPostTitle())
                    .postPublishedAt(externalExternalNaverContent.getPostPublishedAt())
                    .url(externalExternalNaverContent.getUrl())
                    .build();

            internalContentList.add(internalContent);
        }

        return InternalNaverPostVo.builder()
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
            PostEntity naverPost = PostEntity.builder()
                    .companyName(Company.NAVER.getName())
                    .title(rightContent.getPostTitle())
                    .url(rightContent.getUrl())
                    .build();

            postRepository.save(naverPost);
            savedPostCount += 1;
            PostEntity foundNaverPost = postRepository.findByPostId(naverPost.getPostId());
            log.info("[NaverCollector] savePost`s result : {}", foundNaverPost.getTitle());
        }

        return savedPostCount;
    }
}