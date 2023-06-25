package com.techblog.api.post.domain;

import com.techblog.api.post.in.CollectPostIn;
import com.techblog.api.post.model.CollectResult;
import com.techblog.api.post.model.Post;
import com.techblog.common.constant.Company;
import com.techblog.common.exception.domain.CustomAsyncException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    public CollectResult collect(CollectPostIn collectPostIn) {
        List<Company> companyList = collectPostIn.getCompanyList();
        List<Collector> collectorList = new ArrayList<>();
        List<List<Post>> postLists = new ArrayList<>();
        CollectResult finalCollectResult = CollectResult.builder()
                .savedPostCount(0)
                .executedTime(0)
                .build();

        long startTime = System.currentTimeMillis();

        for (Company company : companyList) {
            Collector collector = collectorMap.get(company);
            List<Post> postList = collector.toPost(company);
            collectorList.add(collector);
            postLists.add(postList);
        }

        int postListCount = 0;
        List<CompletableFuture<CollectResult>> completableFutureList = new ArrayList<>();

        for (Collector collector : collectorList) {
            List<Post> postList = postLists.get(postListCount);
            postListCount += 1;

            CompletableFuture<CollectResult> collectResult = CompletableFuture.supplyAsync(
                    () -> collector.savePost(postList))
                    .thenApply(
                            result -> {
                                CollectResult savedResult;
                                try {
                                    savedResult = CollectResult.builder()
                                            .savedPostCount(result.get().getSavedPostCount())
                                            .executedTime(result.get().getExecutedTime())
                                            .build();
                                } catch (InterruptedException | ExecutionException e) {
                                    throw new CustomAsyncException();
                                }
                                return savedResult;
                            }
                    );
            completableFutureList.add(collectResult);
        }

        for (CompletableFuture<CollectResult> collectResult : completableFutureList) {
            try {
                int savedPostCount = finalCollectResult.getSavedPostCount() + collectResult.get().getSavedPostCount();
                finalCollectResult.setSavedPostCount(savedPostCount);
            } catch (ExecutionException | InterruptedException e) {
                throw new CustomAsyncException();
            }
        }

        long executedTime = System.currentTimeMillis() - startTime;
        finalCollectResult.setExecutedTime(executedTime);

        return finalCollectResult;
    }
}