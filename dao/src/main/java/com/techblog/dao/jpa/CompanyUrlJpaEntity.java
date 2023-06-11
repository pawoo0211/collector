package com.techblog.dao.jpa;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "company_urls")
@Getter
public class CompanyUrlJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_url_id")
    private Long companyUrlId;

    @Column(name = "url")
    private String url;

    @Column(name = "company_name")
    private String companyName;

    @Builder
    public CompanyUrlJpaEntity(String url, String companyName) {
        this.url = url;
        this.companyName = companyName;
    }
}