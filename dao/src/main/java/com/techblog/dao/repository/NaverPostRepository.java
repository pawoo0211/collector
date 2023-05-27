package com.techblog.dao.repository;

import com.techblog.dao.document.NaverPostEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaverPostRepository extends MongoRepository<NaverPostEntity, String> {

    NaverPostEntity findByPostId(String postId);
}