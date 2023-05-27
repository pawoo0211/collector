package com.techblog.api.post.domain;

import com.techblog.api.post.in.CollectPostIn;
import com.techblog.common.constant.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j


/**
 * TODO
 * public class CollectManager implements InitializingBean
 */
public class CollectManager<T> implements InitializingBean {

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

    public void collect(CollectPostIn collectPostIn) {
        log.info("[CollectManager] company : {}", collectPostIn.getCompany());
        Collector collector = collectorMap.get(collectPostIn.getCompany());
        T postInfo = (T) collector.toPostInfo(collectPostIn.getUrl());
        log.info("[CollectManager] postInfo`s Class : {}", postInfo.getClass().getName());
        collector.savePost(postInfo);
    }
}