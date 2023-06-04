package com.techblog.api.post.domain.line;

import com.techblog.api.post.domain.Collector;
import com.techblog.api.post.model.CollectResultInfo;
import com.techblog.api.post.model.PostInfo;
import com.techblog.api.post.model.nhn.external.ExternalNhnPostPerLang;
import com.techblog.api.post.model.nhn.external.ExternalNhnPost;
import com.techblog.api.post.model.nhn.external.ExternalNhnPostVo;
import com.techblog.api.post.model.nhn.internal.InternalNhnContentVo;
import com.techblog.api.post.model.nhn.internal.InternalNhnPostVo;
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
        List<InternalNhnPostVo> internalNhnPostVoList = new ArrayList<>();
        List<InternalNhnContentVo> rightNhnContentVoList = new ArrayList<>();
        int savedPostCount = 0;

        for (PostInfo externalNhnPost : postInfoList) {
            InternalNhnPostVo internalNhnPostVo = toInternalNhnPostVo(externalNhnPost);
            internalNhnPostVoList.add(internalNhnPostVo);
        }

        for (InternalNhnPostVo internalNhnPostVo : internalNhnPostVoList) {
            if (postRepository.countByCompanyName(Company.NHN.getName()) == 0) {
                log.info("[NaverCollector] Total nhn post count is 0");
                for (InternalNhnPostVo internalPostVo : internalNhnPostVoList) {
                    savedPostCount += saveRightContent(internalPostVo.getContent());
                }
                break;
            } else {
                rightNhnContentVoList = savePossibilityContent(internalNhnPostVo.getContent());
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
    private <T extends PostInfo> InternalNhnPostVo toInternalNhnPostVo(T externalNhnPost) {
        List<ExternalNhnPostVo> content = externalNhnPost.getContent();
        List<InternalNhnContentVo> internalNhnContentVoList = new ArrayList<>();

        for (ExternalNhnPostVo externalNhnPostVo : content) {
            ExternalNhnPostPerLang externalNhnPostPerLang = externalNhnPostVo.getContent();
            int postId = externalNhnPostPerLang.getPostId();

            InternalNhnContentVo internalNhnContentVo = InternalNhnContentVo.builder()
                    .postId(postId)
                    .url(FIXED_NHN_URL + postId)
                    .title(externalNhnPostPerLang.getTitle())
                    .contentPreview(externalNhnPostVo.getContentPreview())
                    .publishTime(externalNhnPostVo.getPublishTime())
                    .build();

            internalNhnContentVoList.add(internalNhnContentVo);
        }

        return InternalNhnPostVo.builder()
                .content(internalNhnContentVoList)
                .build();
    }

    /**
     * TODO
     * - 객체 탐색 줄이기
     */
    private List<InternalNhnContentVo> savePossibilityContent(List<InternalNhnContentVo> internalNhnContentVoList) {
        List<InternalNhnContentVo> rightInternalContentVoList = new ArrayList<>();
        final LocalDateTime STANDARD_PUBLISH_TIME = toLocalDateTime(internalNhnContentVoList.get(0).getPublishTime());

        for (InternalNhnContentVo internalNhnContentVo : internalNhnContentVoList) {
            LocalDateTime publishTime = toLocalDateTime(internalNhnContentVo.getPublishTime());

            if (publishTime.isBefore(STANDARD_PUBLISH_TIME)) {
                continue;
            }
            rightInternalContentVoList.add(internalNhnContentVo);
        }

        rightInternalContentVoList.remove(0);

        return rightInternalContentVoList;
    }

    private int saveRightContent(List<InternalNhnContentVo> internalNhnContentVo) {
        int savedPostCount = 0;

        for (InternalNhnContentVo rightContent : internalNhnContentVo) {
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