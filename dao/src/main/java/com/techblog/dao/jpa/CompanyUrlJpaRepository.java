package com.techblog.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyUrlJpaRepository extends JpaRepository<CompanyUrlJpaEntity, Long> {

    boolean existsByUrl(String url);

}