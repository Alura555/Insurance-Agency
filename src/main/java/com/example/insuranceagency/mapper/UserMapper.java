package com.example.insuranceagency.mapper;

import com.example.insuranceagency.dto.UserDto;
import com.example.insuranceagency.entity.Role;
import com.example.insuranceagency.entity.User;
import com.example.insuranceagency.repository.RoleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(userDto.getPassword()))")
    @Mapping(target = "role", expression = "java(roleRepository.findByTitle(userDto.getRole()).orElse(null))")
    User mapToUser(UserDto userDto, PasswordEncoder passwordEncoder, RoleRepository roleRepository);


}