package com.techblog.api.post.domain.naver;

import com.techblog.api.post.domain.Collector;
import com.techblog.api.post.model.PostInfo;
import com.techblog.api.post.model.naver.external.Content;
import com.techblog.api.post.model.naver.internal.InternalContent;
import com.techblog.api.post.model.naver.internal.InternalNaverPostInfo;
import com.techblog.api.post.model.naver.external.ExternalNaverPostInfo;
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
            ExternalNaverPostInfo externalNaverPostInfo = dataCommunication.getHttpCall(url, ExternalNaverPostInfo.class);
            externalNaverPostInfoList.add(externalNaverPostInfo);
        }

        return externalNaverPostInfoList;
    }

    @Override
    public void savePost(List<PostInfo> externalNaverPostInfoList) {
        log.info("[NaverCollector] savePost method is started");
        List<InternalNaverPostInfo> internalNaverPostInfoList = new ArrayList<>();
        List<InternalContent> internalContentList;
        List<InternalContent> rightInternalContentList;

        for (PostInfo externalNaverPostInfo : externalNaverPostInfoList) {
            /**
             * TODO
             * - 해당 라인에서 타입 캐스팅이 진행 되는 상태 -> 수정 예정
             */
            InternalNaverPostInfo internalNaverPostInfo = toInternalNaverPostInfo(externalNaverPostInfo);
            internalNaverPostInfoList.add(internalNaverPostInfo);
        }

        for (InternalNaverPostInfo internalNaverPostInfo : internalNaverPostInfoList) {
            if (postRepository.countByCompanyName(Company.NAVER.getName()) == 0) {
                log.info("[NaverCollector] Total naver post count is 0");
                for (InternalNaverPostInfo naverPostInfo : internalNaverPostInfoList) {
                    saveRightContent(naverPostInfo.getContent());
                }
                break;
            } else {
                internalContentList = internalNaverPostInfo.getContent();
                rightInternalContentList = SavePossibilityContent(internalContentList);

                saveRightContent(rightInternalContentList);
            }
        }
    }

    @Override
    public Company getCompany() {
        return Company.NAVER;
    }

    private <T extends PostInfo> InternalNaverPostInfo toInternalNaverPostInfo(T externalNaverPostInfo) {
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