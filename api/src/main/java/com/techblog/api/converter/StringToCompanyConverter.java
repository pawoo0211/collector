package com.techblog.api.converter;

import com.techblog.common.constant.Company;
import com.techblog.common.exception.domain.CompanyNameException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCompanyConverter implements Converter<String, Company> {

    @Override
    public Company convert(String companyName) {
        return Company.valueOf(companyName);
    }
}