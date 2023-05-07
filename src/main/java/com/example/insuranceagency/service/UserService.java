package com.example.insuranceagency.service;

import com.example.insuranceagency.dto.UserDto;
import com.example.insuranceagency.entity.User;

public interface UserService {
    User findByEmail(String email);

    void registerNewUser(UserDto userDto, String role);
}
