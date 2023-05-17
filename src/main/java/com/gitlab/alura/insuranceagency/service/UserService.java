package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.dto.UserDto;
import com.gitlab.alura.insuranceagency.entity.User;

public interface UserService {
    User findByEmail(String email);

    void registerNewUser(UserDto userDto, String role);
}
