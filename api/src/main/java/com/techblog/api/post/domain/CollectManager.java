package com.techblog.api.post.domain;

import com.techblog.api.post.in.CollectPostIn;
import com.techblog.api.post.model.CollectResult;
import com.techblog.api.post.model.Post;
import com.techblog.common.constant.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CollectManager implements InitializingBean {

    private final ApplicationContext applicationContext;
    private Map<Company, Collector> collectorMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws IllegalAccessException {
        Collection<Collector> collectors = applicationContext.getBeansOfType(Collector.class).values();

        for (Collector collector : collectors) {
            if (collectorMap.put(collector.getCompany(), collector) != null) {
                throw new IllegalAccessException("Put processing exception occurred in CollectManager");
            }
        }
    }

    /**
     * 멀티 쓰레딩 처리 전 저장 소요 시간 : 15753ms
     * 멀티 쓰레딩 처리 전 저장 소요 시간 : ms
     */
    public CollectResult collect(CollectPostIn collectPostIn) {
        List<Company> companyList = collectPostIn.getCompanyList();
        CollectResult finalCollectResult = CollectResult.builder()
                .savedPostCount(0)
                .executedTime(0)
                .build();

        long startTime = System.currentTimeMillis();

        for (Company company : companyList) {
            Collector collector = collectorMap.get(company);
            List<Post> postList = collector.toPost(company);
            CollectResult collectResult = collector.savePost(postList);

            int savedPostCount = finalCollectResult.getSavedPostCount() + collectResult.getSavedPostCount();
            finalCollectResult.setExecutedTime(savedPostCount);
        }

        long executedTime = System.currentTimeMillis() - startTime;
        finalCollectResult.setExecutedTime(executedTime);

        return finalCollectResult;
    }
}