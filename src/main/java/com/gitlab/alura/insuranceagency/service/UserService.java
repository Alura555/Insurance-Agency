package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.dto.RoleDto;
import com.gitlab.alura.insuranceagency.dto.UserDto;
import com.gitlab.alura.insuranceagency.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    User findByEmail(String email);
    User findById(Long id);

    Long registerNewUser(UserDto userDto, String role);

    Page<UserDto> getAllActive(Pageable pageable);

    UserDto getUserDtoById(Long id);

    void deleteById(Long id);

    Long updateUser(UserDto userDto, String roleTitle);

    List<RoleDto> getUserRoles();
}
