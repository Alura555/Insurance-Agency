package com.example.insuranceagency.service.implementation;

import com.example.insuranceagency.detail.UserDetailsImpl;
import com.example.insuranceagency.dto.UserDto;
import com.example.insuranceagency.entity.User;
import com.example.insuranceagency.exception.UserRegistrationException;
import com.example.insuranceagency.mapper.UserMapper;
import com.example.insuranceagency.repository.RoleRepository;
import com.example.insuranceagency.repository.UserRepository;
import com.example.insuranceagency.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElse(null);
        if (user == null){
            throw new UsernameNotFoundException("User not found!");
        }
        return new UserDetailsImpl(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public void registerNewUser(UserDto userDto, String role) {
        userDto.setRole(role);
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()){
            throw new UserRegistrationException("username", "Username already exists");
        }
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new UserRegistrationException("email", "Email already exists");
        }
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.YEAR, -18);

        if (userDto.getBirthday().after(calendar.getTime())) {
            throw new UserRegistrationException("birthday", "You must be at least 18 years old to register");
        }

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new UserRegistrationException("confirmPassword", "Passwords do not match");
        }
        User user = userMapper.mapToUser(userDto, passwordEncoder, roleRepository);
        user.setActive(true);
        userRepository.save(user);
    }
}
