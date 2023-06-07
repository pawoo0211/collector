package com.techblog.api.post.domain.nhn;

import com.techblog.api.post.domain.Collector;
import com.techblog.api.post.model.CollectResultInfo;
import com.techblog.api.post.model.PostInfo;
import com.techblog.api.post.model.nhn.external.ExternalNhnPostPerLang;
import com.techblog.api.post.model.nhn.external.ExternalNhnPost;
import com.techblog.api.post.model.nhn.external.ExternalNhnPostVo;
import com.techblog.api.post.model.nhn.internal.InternalNhnContent;
import com.techblog.api.post.model.nhn.internal.InternalNhnPost;
import com.techblog.common.constant.Company;
import com.techblog.common.webclient.DataCommunication;
import com.techblog.dao.document.PostEntity;
import com.techblog.dao.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NhnCollector implements Collector {

    private final PostRepository postRepository;
    private final DataCommunication dataCommunication;
    private final String FIXED_NHN_URL = "https://meetup.nhncloud.com/posts/";

    @Override
    public List<PostInfo> toPostInfo(Company company) {
        List<String> NhnPostUrlList = company.getUrlList();
        List<PostInfo> externalNhnPostInfoList = new ArrayList<>();

        log.info("[NaverCollector] Data communication is started");
        for (String url : NhnPostUrlList) {
            ExternalNhnPost externalNhnPost = dataCommunication.getHttpCall(url, ExternalNhnPost.class);
            externalNhnPostInfoList.add(externalNhnPost);
        }

        return externalNhnPostInfoList;
    }

    @Override
    public CollectResultInfo savePost(List<PostInfo> postInfoList) {
        log.info("[NhnCollector] savePost method is started");
        List<InternalNhnPost> internalNhnPostList = new ArrayList<>();
        List<InternalNhnContent> rightNhnContentVoList = new ArrayList<>();
        int savedPostCount = 0;

        for (PostInfo externalNhnPost : postInfoList) {
            InternalNhnPost internalNhnPost = toInternalNhnPostVo(externalNhnPost);
            internalNhnPostList.add(internalNhnPost);
        }

        for (InternalNhnPost internalNhnPost : internalNhnPostList) {
            if (postRepository.countByCompanyName(Company.NHN.getName()) == 0) {
                log.info("[NaverCollector] Total nhn post count is 0");
                for (InternalNhnPost internalPostVo : internalNhnPostList) {
                    savedPostCount += saveRightContent(internalPostVo.getContent());
                }
                break;
            } else {
                rightNhnContentVoList = savePossibilityContent(internalNhnPost.getContent());
                savedPostCount += saveRightContent(rightNhnContentVoList);
            }
        }

        return CollectResultInfo.builder()
                .savedPostCount(savedPostCount)
                .executedTime(0L)
                .build();
    }

    @Override
    public Company getCompany() {
        return Company.NHN;
    }

    /**
     * TODO
     * - PostVo -> Post로 변경
     */
    private <T extends PostInfo> InternalNhnPost toInternalNhnPostVo(T externalNhnPost) {
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
                    .publishTime(externalNhnPostVo.getPublishTime())
                    .build();

            internalNhnContentList.add(internalNhnContent);
        }

        return InternalNhnPost.builder()
                .content(internalNhnContentList)
                .build();
    }

    /**
     * TODO
     * - 객체 탐색 줄이기
     */
    private List<InternalNhnContent> savePossibilityContent(List<InternalNhnContent> internalNhnContentList) {
        List<InternalNhnContent> rightInternalContentVoList = new ArrayList<>();
        final LocalDateTime STANDARD_PUBLISH_TIME = toLocalDateTime(internalNhnContentList.get(0).getPublishTime());

        for (InternalNhnContent internalNhnContent : internalNhnContentList) {
            LocalDateTime publishTime = toLocalDateTime(internalNhnContent.getPublishTime());

            if (publishTime.isBefore(STANDARD_PUBLISH_TIME)) {
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
            PostEntity nhnPost = PostEntity.builder()
                    .companyName(Company.NHN.getName())
                    .title(rightContent.getTitle())
                    .url(rightContent.getUrl())
                    .build();

            postRepository.save(nhnPost);
            savedPostCount += 1;
            PostEntity foundNaverPost = postRepository.findByPostId(nhnPost.getPostId());
            log.info("[NhnCollector] savePost`s result : {}", foundNaverPost.getTitle());
        }
        return savedPostCount;
    }

    /**
     * TODO
     * - common 모듈로 빼기
     */
    private LocalDateTime toLocalDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeString, formatter);
        LocalDateTime dateTime = offsetDateTime.toLocalDateTime();

        return dateTime;
    }
}