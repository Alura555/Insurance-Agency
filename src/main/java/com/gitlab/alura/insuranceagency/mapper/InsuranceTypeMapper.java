package com.gitlab.alura.insuranceagency.mapper;

import com.gitlab.alura.insuranceagency.dto.InsuranceTypeDto;
import com.gitlab.alura.insuranceagency.entity.InsuranceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface InsuranceTypeMapper {
    @Mapping(target = "title", expression = "java(entity.getTitle() + \" insurance\")")
    InsuranceTypeDto toDto(InsuranceType entity);
}
