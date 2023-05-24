package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.dto.RoleDto;
import com.gitlab.alura.insuranceagency.dto.UserDto;
import com.gitlab.alura.insuranceagency.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    User getByEmail(String email);
    User getById(Long id);

    Long registerUser(UserDto userDto, String role, Long companyId);
    void registerClient(UserDto userDto);

    Page<UserDto> getAllActive(Pageable pageable);

    UserDto getUserDtoById(Long id);

    void deleteById(Long id);

    Long updateUser(UserDto userDto, String roleTitle);

    List<RoleDto> getUserRoles();

}
