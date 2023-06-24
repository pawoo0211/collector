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

    /**
     * 멀티 쓰레딩 처리 전 저장 소요 시간 : 15753ms
     * 멀티 쓰레딩 처리 전 저장 소요 시간 : 14195ms
     */
    public CollectResult collect(CollectPostIn collectPostIn) throws ExecutionException, InterruptedException {
        List<Company> companyList = collectPostIn.getCompanyList();
        List<Collector> collectors = new ArrayList<>();
        List<List<Post>> posts = new ArrayList<>();
        CollectResult finalCollectResult = CollectResult.builder()
                .savedPostCount(0)
                .executedTime(0)
                .build();

        long startTime = System.currentTimeMillis();

        for (Company company : companyList) {
            /**
             * "List"의 특성인 index(순서)를 이용
             * 변환까지는 동기 처리 후 글을 수집하는 것에서 비동기 처리 진행
             */
            Collector collector = collectorMap.get(company);
            List<Post> postList = collector.toPost(company);
            collectors.add(collector);
            posts.add(postList);
        }

        int postListCount = 0;
        List<CompletableFuture<CollectResult>> completableFutures = new ArrayList<>();

        for (Collector collector : collectors) {
            /**
             * 비동기 처리가 진행되기 전에 count + 1
             */
            List<Post> postList = posts.get(postListCount);
            postListCount += 1;

            /**
             * TODO
             * - 비동기 예외 처리
             */
            CompletableFuture<CollectResult> collectResult = CompletableFuture.supplyAsync(
                    () -> collector.savePost(postList))
                    .thenApply(
                            result -> {
                                CollectResult savedResult = null;
                                try {
                                    savedResult = CollectResult.builder()
                                            .savedPostCount(result.get().getSavedPostCount())
                                            .executedTime(result.get().getExecutedTime())
                                            .build();
                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                }

                                return savedResult;
                            }
                    );
            completableFutures.add(collectResult);
        }

        for (CompletableFuture<CollectResult> collectResult : completableFutures) {
            int savedPostCount = finalCollectResult.getSavedPostCount() + collectResult.get().getSavedPostCount();
            finalCollectResult.setSavedPostCount(savedPostCount);
        }

        long executedTime = System.currentTimeMillis() - startTime;
        finalCollectResult.setExecutedTime(executedTime);

        return finalCollectResult;
    }
}