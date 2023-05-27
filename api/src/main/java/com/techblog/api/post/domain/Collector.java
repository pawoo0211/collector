package com.techblog.api.post.domain;

import com.techblog.common.constant.Company;
import org.springframework.stereotype.Component;

@Component
public interface Collector<T> {

    /**
     * TODO
     * 제네릭으로 변환하지 말고 PostInfo라는 인터페이스 혹은 추상클래스로 감싸서 반환하기 -> 타입 캐스팅을 명시하지 않기 위해
     * 제네릭으로 반환하지 말기
     * PostInfo toPostInfo
     */
    public T toPostInfo(String url);

    public void savePost(T postInfo);
    public Company getCompany();

}