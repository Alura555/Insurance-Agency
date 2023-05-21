package com.gitlab.alura.insuranceagency.config;

import com.gitlab.alura.insuranceagency.mapper.DocumentTypeMapper;
import com.gitlab.alura.insuranceagency.mapper.InsuranceTypeMapper;
import com.gitlab.alura.insuranceagency.mapper.OfferMapper;
import com.gitlab.alura.insuranceagency.mapper.PolicyMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapStructConfig {
    @Bean
    public OfferMapper offerMapper() {
        return Mappers.getMapper(OfferMapper.class);
    }
    @Bean
    public InsuranceTypeMapper insuranceTypeMapper() {
        return Mappers.getMapper(InsuranceTypeMapper.class);
    }

    @Bean
    public PolicyMapper policyMapper(){
        return Mappers.getMapper(PolicyMapper.class);
    }

    @Bean
    public DocumentTypeMapper documentTypeMapper(){
        return Mappers.getMapper(DocumentTypeMapper.class);
    }
}
