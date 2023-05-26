package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.dto.RoleDto;
import com.gitlab.alura.insuranceagency.dto.UserDto;
import com.gitlab.alura.insuranceagency.entity.Role;
import com.gitlab.alura.insuranceagency.entity.User;
import com.gitlab.alura.insuranceagency.exception.InvalidInputException;
import com.gitlab.alura.insuranceagency.exception.NotFoundException;
import com.gitlab.alura.insuranceagency.mapper.RoleMapper;
import com.gitlab.alura.insuranceagency.mapper.UserMapper;
import com.gitlab.alura.insuranceagency.repository.RoleRepository;
import com.gitlab.alura.insuranceagency.repository.UserRepository;
import com.gitlab.alura.insuranceagency.service.implementation.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest extends BaseClassTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RoleMapper roleMapper;
    @Mock
    private CompanyService companyService;
    @InjectMocks
    private UserDetailsServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetByEmailExistingUser() {
        User user = new User();
        user.setEmail(USER_EMAIL);
        when(userRepository.findByEmailAndIsActive(USER_EMAIL, true)).thenReturn(Optional.of(user));

        User result = userService.getByEmail(USER_EMAIL);

        assertNotNull(result);
        assertEquals(USER_EMAIL, result.getEmail());
    }

    @Test
    public void testGetByEmailNonExistingUser() {
        when(userRepository.findByEmailAndIsActive(USER_EMAIL, true)).thenReturn(Optional.empty());

        User actualUser = userService.getByEmail(USER_EMAIL);

        assertNull(actualUser);
    }

    @Test
    public void testGetByIdExistingUser() {
        User expectedUser = new User();
        expectedUser.setId(USER_ID);

        when(userRepository.findByIdAndIsActive(USER_ID, true)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getById(USER_ID);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testGetByIdNonExistingUser() {
        when(userRepository.findByIdAndIsActive(USER_ID, true)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getById(USER_ID));

        assertEquals("User not found!", exception.getMessage());
    }

    @Test
    public void testRegisterCompanyManager() {
        UserDto userDto = new UserDto();
        userDto.setEmail(USER_EMAIL);
        userDto.setBirthday(addYears(new Date(), -20));
        userDto.setUsername(USERNAME);
        userDto.setPassword(PASSWORD);
        userDto.setConfirmPassword(PASSWORD);

        User newUser = new User();
        newUser.setId(USER_ID);

        Role role = new Role();
        RoleDto roleDto = new RoleDto();

        when(roleRepository.findByTitle(ROLE_TITLE_COMPANY_MANAGER)).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(roleDto);
        when(userMapper.mapToUser(userDto, passwordEncoder)).thenReturn(newUser);
        when(userRepository.findByUsernameAndIsActive(USERNAME, true)).thenReturn(Optional.empty());
        when(userRepository.findByEmailAndIsActive(USER_EMAIL, true)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        Long actualUserId = userService.registerUser(userDto, ROLE_TITLE_COMPANY_MANAGER, COMPANY_ID);

        assertEquals(USER_ID, actualUserId);
        verify(userRepository).save(any(User.class));
        verify(companyService).addCompanyManager(newUser, COMPANY_ID);
    }

    @Test
    public void testRegisterManagerWithInvalidBirthday() {
        UserDto userDto = new UserDto();
        userDto.setEmail(MANAGER_EMAIL);
        userDto.setBirthday(new Date());
        userDto.setUsername(USERNAME);
        userDto.setPassword(PASSWORD);
        userDto.setConfirmPassword(PASSWORD);

        User newUser = new User();
        newUser.setId(USER_ID);

        Role role = new Role();
        RoleDto roleDto = new RoleDto();

        when(roleRepository.findByTitle(ROLE_TITLE_MANAGER)).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(roleDto);
        when(userMapper.mapToUser(userDto, passwordEncoder)).thenReturn(newUser);
        when(userRepository.findByUsernameAndIsActive(USERNAME, true)).thenReturn(Optional.empty());
        when(userRepository.findByEmailAndIsActive(MANAGER_EMAIL, true)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        Exception exception = assertThrows(InvalidInputException.class,
                () -> userService.registerUser(userDto, ROLE_TITLE_MANAGER, null));

        assertEquals("You must be at least 18 years old to register", exception.getMessage());
        verify(userRepository, times(0)).save(any(User.class));
        verify(companyService, times(0)).addCompanyManager(newUser, null);
        verify(userMapper, times(0)).mapToUser(any(UserDto.class), eq(passwordEncoder));
    }

    @Test
    public void testRegisterManagerWithInvalidEmail() {
        UserDto userDto = new UserDto();
        userDto.setEmail(MANAGER_EMAIL);
        userDto.setBirthday(addYears(new Date(), -20));
        userDto.setUsername(USERNAME);
        userDto.setPassword(PASSWORD);
        userDto.setConfirmPassword(PASSWORD);

        User newUser = new User();
        newUser.setId(USER_ID);

        Role role = new Role();
        RoleDto roleDto = new RoleDto();

        when(roleRepository.findByTitle(ROLE_TITLE_MANAGER)).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(roleDto);
        when(userMapper.mapToUser(userDto, passwordEncoder)).thenReturn(newUser);
        when(userRepository.findByUsernameAndIsActive(USERNAME, true)).thenReturn(Optional.empty());
        when(userRepository.findByEmailAndIsActive(MANAGER_EMAIL, true)).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        Exception exception = assertThrows(InvalidInputException.class,
                () -> userService.registerUser(userDto, ROLE_TITLE_MANAGER, null));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(0)).save(any(User.class));
        verify(companyService, times(0)).addCompanyManager(newUser, null);
        verify(userMapper, times(0)).mapToUser(any(UserDto.class), eq(passwordEncoder));
    }

    @Test
    public void testRegisterManagerWithInvalidUsername() {
        UserDto userDto = new UserDto();
        userDto.setEmail(MANAGER_EMAIL);
        userDto.setBirthday(addYears(new Date(), -20));
        userDto.setUsername(USERNAME);
        userDto.setPassword(PASSWORD);
        userDto.setConfirmPassword(PASSWORD);

        User newUser = new User();
        newUser.setId(USER_ID);

        Role role = new Role();
        RoleDto roleDto = new RoleDto();

        when(roleRepository.findByTitle(ROLE_TITLE_MANAGER)).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(roleDto);
        when(userMapper.mapToUser(userDto, passwordEncoder)).thenReturn(newUser);
        when(userRepository.findByUsernameAndIsActive(USERNAME, true)).thenReturn(Optional.of(new User()));
        when(userRepository.findByEmailAndIsActive(MANAGER_EMAIL, true)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        Exception exception = assertThrows(InvalidInputException.class,
                () -> userService.registerUser(userDto, ROLE_TITLE_MANAGER, null));

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, times(0)).save(any(User.class));
        verify(companyService, times(0)).addCompanyManager(newUser, null);
        verify(userMapper, times(0)).mapToUser(any(UserDto.class), eq(passwordEncoder));
    }


    @Test
    void registerClient() {
        UserDto userDto = new UserDto();
        userDto.setEmail(CLIENT_EMAIL);
        userDto.setBirthday(addYears(new Date(), -20));
        userDto.setUsername(USERNAME);
        userDto.setPassword(PASSWORD);
        userDto.setConfirmPassword(PASSWORD);

        User newUser = new User();
        newUser.setId(USER_ID);
        Role role = new Role();
        RoleDto roleDto = new RoleDto();

        when(roleRepository.findByTitle(ROLE_TITLE_CLIENT)).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(roleDto);
        when(userMapper.mapToUser(userDto, passwordEncoder)).thenReturn(newUser);
        when(userRepository.findByUsernameAndIsActive(USERNAME, true)).thenReturn(Optional.empty());
        when(userRepository.findByEmailAndIsActive(CLIENT_EMAIL, true)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        userService.registerClient(userDto);

        verify(userRepository, atLeastOnce()).save(newUser);
        verify(roleRepository, times(1)).findByTitle(ROLE_TITLE_CLIENT);
        verify(userMapper, times(1)).mapToUser(userDto, passwordEncoder);
        verify(companyService, times(0)).addCompanyManager(newUser, null);
    }

    @Test
    void getAllActiveUsers() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        String username1 = "username1";
        String userEmail1 = "user1@example.com";
        String username2 = "username2";
        String userEmail2 = "user2@example.com";

        User user1 = new User();
        user1.setEmail(userEmail1);
        user1.setUsername(username1);

        User user2 = new User();
        user2.setEmail(userEmail2);
        user2.setUsername(username2);

        List<User> mockUsers = Arrays.asList(user1, user2);
        Page<User> mockUserPage = new PageImpl<>(mockUsers);
        when(userRepository.findAllByIsActive(any(Pageable.class), eq(true))).thenReturn(mockUserPage);

        UserDto userDto1 = new UserDto();
        userDto1.setEmail(userEmail1);
        userDto1.setUsername(username1);

        UserDto userDto2 = new UserDto();
        userDto2.setEmail(userEmail2);
        userDto2.setUsername(username2);

        List<UserDto> expectedUserDtoList = Arrays.asList(userDto1, userDto2);
        Page<UserDto> expectedUserDtoPage = new PageImpl<>(expectedUserDtoList, pageable, mockUsers.size());

        when(userMapper.toDto(user1)).thenReturn(userDto1);
        when(userMapper.toDto(user2)).thenReturn(userDto2);

        Page<UserDto> result = userService.getAllActive(pageable);

        assertEquals(expectedUserDtoPage, result);
    }

    @Test
    void getAllActiveUsersWithEmpty() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        Page<User> mockUserPage = new PageImpl<>(Collections.emptyList());
        when(userRepository.findAllByIsActive(any(Pageable.class), eq(true))).thenReturn(mockUserPage);

        Page<UserDto> expectedUserDtoPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        Page<UserDto> result = userService.getAllActive(pageable);

        assertEquals(expectedUserDtoPage, result);
    }

    @Test
    void getUserDtoByIdTest() {
        User user = new User();

        UserDto expectedUserDto = new UserDto();

        when(userRepository.findByIdAndIsActive(USER_ID, true)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(expectedUserDto);

        UserDto result = userService.getUserDtoById(USER_ID);

        assertEquals(expectedUserDto, result);
        verify(userRepository, times(1)).findByIdAndIsActive(USER_ID, true);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void getUserDtoByNonExistingIdTest() {
        User user = new User();
        when(userRepository.findByIdAndIsActive(USER_ID, true)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getUserDtoById(USER_ID));

        verify(userRepository, times(1)).findByIdAndIsActive(USER_ID, true);
        verify(userMapper, times(0)).toDto(user);
    }


    @Test
    void deleteUserByIdTest() {
        User user = new User();
        user.setActive(true);

        when(userRepository.findByIdAndIsActive(USER_ID, true)).thenReturn(Optional.of(user));

        userService.deleteById(USER_ID);

        assertEquals(false, user.getActive());
        verify(userRepository, times(1)).findByIdAndIsActive(USER_ID, true);
    }

    @Test
    void deleteUserByNonExistingIdTest() {
        when(userRepository.findByIdAndIsActive(USER_ID, true)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getUserDtoById(USER_ID));

        verify(userRepository, times(1)).findByIdAndIsActive(USER_ID, true);
    }

}
