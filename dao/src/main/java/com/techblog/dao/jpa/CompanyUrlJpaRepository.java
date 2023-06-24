package com.techblog.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyUrlJpaRepository extends JpaRepository<CompanyUrlJpaEntity, Long> {

    boolean existsByUrl(String url);
    List<CompanyUrlJpaEntity> findAllByCompanyName(String companyName);

}