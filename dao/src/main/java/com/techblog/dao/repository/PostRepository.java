package com.techblog.dao.repository;

import com.techblog.dao.document.PostEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<PostEntity, String> {

    PostEntity findByPostId(String postId);
    int countByCompanyName(String companyName);

}