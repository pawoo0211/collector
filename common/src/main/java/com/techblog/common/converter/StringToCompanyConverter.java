package com.techblog.common.converter;

import com.techblog.common.constant.Company;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCompanyConverter implements Converter<String, Company> {

    @Override
    public Company convert(String company) {
        return Company.valueOf(company);
    }
}