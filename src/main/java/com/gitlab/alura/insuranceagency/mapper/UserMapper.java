package com.gitlab.alura.insuranceagency.mapper;

import com.gitlab.alura.insuranceagency.dto.UserDto;
import com.gitlab.alura.insuranceagency.repository.RoleRepository;
import com.gitlab.alura.insuranceagency.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(userDto.getPassword()))")
    @Mapping(target = "role", expression = "java(roleRepository.findByTitle(userDto.getRole()).orElse(null))")
    User mapToUser(UserDto userDto, PasswordEncoder passwordEncoder, RoleRepository roleRepository);

    @Mapping(target = "role", source = "role.title")
    @Mapping(target = "id", source = "id")
    UserDto toDto(User user);
}