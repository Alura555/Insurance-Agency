package com.gitlab.alura.insuranceagency.service.implementation;

import com.gitlab.alura.insuranceagency.repository.RoleRepository;
import com.gitlab.alura.insuranceagency.repository.UserRepository;
import com.gitlab.alura.insuranceagency.security.UserDetailsImpl;
import com.gitlab.alura.insuranceagency.service.UserService;
import com.gitlab.alura.insuranceagency.dto.UserDto;
import com.gitlab.alura.insuranceagency.entity.User;
import com.gitlab.alura.insuranceagency.exception.InvalidInputException;
import com.gitlab.alura.insuranceagency.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service
@Transactional
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
            throw new InvalidInputException("username", "Username already exists");
        }
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new InvalidInputException("email", "Email already exists");
        }
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.YEAR, -18);

        if (userDto.getBirthday().after(calendar.getTime())) {
            throw new InvalidInputException("birthday", "You must be at least 18 years old to register");
        }

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new InvalidInputException("confirmPassword", "Passwords do not match");
        }
        User user = userMapper.mapToUser(userDto, passwordEncoder, roleRepository);
        user.setActive(true);
        userRepository.save(user);
    }
}
