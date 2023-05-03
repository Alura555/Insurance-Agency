package com.example.insuranceagency.configs;

import com.example.insuranceagency.mappers.InsuranceTypeMapper;
import com.example.insuranceagency.mappers.OfferMapper;
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
}
