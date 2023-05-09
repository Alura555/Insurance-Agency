package com.example.insuranceagency.config;

import com.example.insuranceagency.mapper.InsuranceTypeMapper;
import com.example.insuranceagency.mapper.OfferMapper;
import com.example.insuranceagency.mapper.PolicyMapper;
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
}
