package com.techblog.api.post.domain;

import com.techblog.api.post.in.CollectPostIn;
import com.techblog.api.post.model.CollectResultInfo;
import com.techblog.api.post.model.PostInfo;
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

    public CollectResultInfo collect(CollectPostIn collectPostIn) {
        log.info("[CollectManager] company : {}", collectPostIn.getCompany());
        long startTime = System.currentTimeMillis();

        Collector collector = collectorMap.get(collectPostIn.getCompany());
        List<PostInfo> postInfoList = collector.toPostInfo(collectPostIn.getCompany());
        log.info("[CollectManager] postInfo`s Class : {}", postInfoList.getClass().getTypeName());
        CollectResultInfo collectResultInfo = collector.savePost(postInfoList);

        long executedTime = System.currentTimeMillis() - startTime;
        collectResultInfo.setExecutedTime(executedTime);
        return collectResultInfo;
    }
}