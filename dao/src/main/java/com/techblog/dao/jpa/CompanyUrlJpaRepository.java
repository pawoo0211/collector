package com.techblog.dao.jpa;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyUrlJpaRepository extends JpaRepository<CompanyUrlJpaEntity, Long> {

    boolean existsByUrl(String url);

    @Cacheable(value = "CompanyUrls", key = "#p0")
    List<CompanyUrlJpaEntity> findAllByCompanyName(String companyName);
}