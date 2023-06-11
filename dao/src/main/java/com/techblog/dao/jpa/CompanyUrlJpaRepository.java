package com.techblog.dao.jpa;

import com.techblog.dao.jpa.CompanyUrlJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyUrlJpaRepository extends JpaRepository<CompanyUrlJpaEntity, Long> {

}