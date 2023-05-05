package com.example.insuranceagency.mapper;

import com.example.insuranceagency.dto.InsuranceTypeDto;
import com.example.insuranceagency.entity.InsuranceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface InsuranceTypeMapper {
    @Mapping(target = "title", expression = "java(entity.getTitle() + \" insurance\")")
    InsuranceTypeDto toDto(InsuranceType entity);
}
