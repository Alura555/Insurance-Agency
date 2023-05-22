package com.gitlab.alura.insuranceagency.mapper;

import com.gitlab.alura.insuranceagency.dto.UserDto;
import com.gitlab.alura.insuranceagency.repository.RoleRepository;
import com.gitlab.alura.insuranceagency.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(uses = RoleMapper.class)
public interface UserMapper {

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(userDto.getPassword()))")
    User mapToUser(UserDto userDto, PasswordEncoder passwordEncoder);
    @Mapping(target = "password")
    User mapToUser(UserDto userDto);

    @Mapping(target = "id", source = "id")
    UserDto toDto(User user);
}