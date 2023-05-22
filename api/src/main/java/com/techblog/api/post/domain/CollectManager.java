package com.techblog.api.post.domain;

import com.techblog.api.post.in.CollectPostIn;
import com.techblog.common.constant.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CollectManager implements InitializingBean {

    private final ApplicationContext applicationContext;
    private Map<Company, Collector> collectorMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws IllegalAccessException {
        Collection<Collector> collectors = applicationContext.getBeansOfType(Collector.class).values();

        for (Collector collector : collectors) {
            if (collectorMap.put(collector.getCompany(), collector) == null) {
                throw new IllegalAccessException("Put processing exception occurred in CollectManager");
            }
        }
    }

    public void collect(CollectPostIn collectPostIn) {
        Collector collector = collectorMap.get(collectPostIn.getCompany());
        collector.savePost(collectPostIn.getData());
    }
}