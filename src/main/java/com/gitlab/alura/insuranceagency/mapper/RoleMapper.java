package com.gitlab.alura.insuranceagency.mapper;

import com.gitlab.alura.insuranceagency.dto.RoleDto;
import com.gitlab.alura.insuranceagency.entity.Role;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {
    RoleDto toDto(Role role);

    Role toEntity(RoleDto roleDto);
}
