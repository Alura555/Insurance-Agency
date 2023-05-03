package com.example.insuranceagency.mappers;

import com.example.insuranceagency.dtos.InsuranceTypeDto;
import com.example.insuranceagency.entities.InsuranceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface InsuranceTypeMapper {
    @Mapping(target = "title", expression = "java(entity.getTitle() + \" insurance\")")
    InsuranceTypeDto toDto(InsuranceType entity);
}
