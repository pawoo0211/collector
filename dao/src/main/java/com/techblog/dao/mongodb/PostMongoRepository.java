package com.techblog.dao.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostMongoRepository extends MongoRepository<PostMongoEntity, String> {

    PostMongoEntity findByPostId(String postId);
    int countByCompanyName(String companyName);
}