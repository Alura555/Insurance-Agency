package com.gitlab.alura.insuranceagency.mapper;

import com.gitlab.alura.insuranceagency.dto.CompanyDto;
import com.gitlab.alura.insuranceagency.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {UserMapper.class, RoleMapper.class})
public interface CompanyMapper {
    CompanyDto toDto(Company company);

    @Mapping(target = "managers", ignore = true)
    Company toEntity(CompanyDto companyDto);
}
