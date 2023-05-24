package com.gitlab.alura.insuranceagency.service.implementation;

import com.gitlab.alura.insuranceagency.dto.RoleDto;
import com.gitlab.alura.insuranceagency.entity.Role;
import com.gitlab.alura.insuranceagency.exception.NotFoundException;
import com.gitlab.alura.insuranceagency.mapper.RoleMapper;
import com.gitlab.alura.insuranceagency.repository.RoleRepository;
import com.gitlab.alura.insuranceagency.repository.UserRepository;
import com.gitlab.alura.insuranceagency.security.UserDetailsImpl;
import com.gitlab.alura.insuranceagency.service.CompanyService;
import com.gitlab.alura.insuranceagency.service.UserService;
import com.gitlab.alura.insuranceagency.dto.UserDto;
import com.gitlab.alura.insuranceagency.entity.User;
import com.gitlab.alura.insuranceagency.exception.InvalidInputException;
import com.gitlab.alura.insuranceagency.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService, UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    private final CompanyService companyService;
    public UserDetailsServiceImpl(UserRepository userRepository,
                                  RoleRepository roleRepository,
                                  PasswordEncoder passwordEncoder,
                                  UserMapper userMapper,
                                  RoleMapper roleMapper,
                                  CompanyService companyService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.companyService = companyService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by email: {}", username);
        User user = getByEmail(username);
        if (user == null){
            logger.warn("User not found for email: {}", username);
            throw new UsernameNotFoundException("User not found!");
        }
        logger.info("User loaded successfully: {}", user.getEmail());
        return new UserDetailsImpl(user);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmailAndIsActive(email, true).orElse(null);
    }

    @Override
    public User getById(Long id) {
        logger.info("Finding user by ID: {}", id);
        return userRepository.findByIdAndIsActive(id, true)
                .orElseThrow(() -> {
                    logger.error("User not found for ID: {}", id);
                    return new NotFoundException("User not found!");
                });
    }

    @Override
    public Long registerUser(UserDto userDto, String roleTitle, Long companyId) {
        logger.info("Registering new user with email: {} with role: {}", userDto.getEmail(), roleTitle);
        User newUser = createUser(userDto, roleTitle, true);
        newUser = userRepository.save(newUser);
        if (companyId != null && companyId != 0L) {
            companyService.addCompanyManager(newUser, companyId);
        }
        logger.info("Registered new user with email: {} and id: {}", userDto.getEmail(), newUser.getId());
        return newUser.getId();
    }

    @Override
    public void registerClient(UserDto userDto) {
        registerUser(userDto, "CLIENT", null);
    }


    private void validateUserData(UserDto userDto, Boolean isNew) throws InvalidInputException{
        if (isNew && userRepository.findByUsernameAndIsActive(userDto.getUsername(), true).isPresent()){
            throw new InvalidInputException("username", "Username already exists");
        }
        if (isNew && getByEmail(userDto.getEmail()) != null){
            throw new InvalidInputException("email", "Email already exists");
        }
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.YEAR, -18);

        if (userDto.getBirthday() == null || userDto.getBirthday().after(calendar.getTime())) {
            throw new InvalidInputException("birthday", "You must be at least 18 years old to register");
        }

        if (isNew && !userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new InvalidInputException("confirmPassword", "Passwords do not match");
        }
    }

    @Override
    public Page<UserDto> getAllActive(Pageable pageable) {
        Page<User> userPage = userRepository.findAllByIsActive(pageable, true);
        List<UserDto> userDtoList = userPage
                .getContent()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(userDtoList, pageable, userPage.getTotalElements());
    }

    @Override
    public UserDto getUserDtoById(Long id) {
        User user = getById(id);
        return userMapper.toDto(user);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting user by ID: {}", id);
        User user = getById(id);
        user.setActive(false);
        userRepository.save(user);
        logger.info("User with ID {} deleted successfully", id);
    }

    @Override
    public Long updateUser(UserDto userDto, String roleTitle) throws InvalidInputException{
        logger.info("Updating user with ID: {}", userDto.getId());
        User oldUser = getById(userDto.getId());
        User updatedUser = createUser(userDto, roleTitle, false);
        updatedUser.setPassword(oldUser.getPassword());
        logger.info("User updated successfully with ID: {}", updatedUser.getId());
        return userRepository.save(updatedUser).getId();
    }


    private User createUser(UserDto userDto, String roleTitle, Boolean isNew) throws InvalidInputException{
        Role role = roleRepository.findByTitle(roleTitle).orElseThrow(NotFoundException::new);
        userDto.setRole(roleMapper.toDto(role));
        validateUserData(userDto, isNew);
        User user = null;
        if (isNew){
            user = userMapper.mapToUser(userDto, passwordEncoder);
        }else {
            user = userMapper.mapToUser(userDto);
        }

        user.setActive(true);
        return user;
    }
    @Override
    public List<RoleDto> getUserRoles() {
        List<Role> roles = roleRepository.findAllByIsActive(true);
        return roles
                .stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }
}
