package com.example.insuranceagency.service;

import com.example.insuranceagency.entity.User;

public interface UserService {
    User findByEmail(String email);
}
