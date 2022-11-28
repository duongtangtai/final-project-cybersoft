package com.example.riraproject.profile.service;

import com.example.riraproject.profile.dto.ChangePasswordForm;
import com.example.riraproject.role.model.Role;
import com.example.riraproject.role.util.RoleUtil;
import com.example.riraproject.security.dto.LoginResultDto;
import com.example.riraproject.security.util.JwtUtil;
import com.example.riraproject.user.dto.UserDto;
import com.example.riraproject.user.model.User;
import com.example.riraproject.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.ValidationException;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @Mock private UserService userService;
    @Autowired private ModelMapper mapper;
    @Mock private JwtUtil jwtUtil;
    @Autowired private MessageSource messageSource;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    private ProfileServiceImpl service;

    @BeforeEach
    void init() {
        service = new ProfileServiceImpl(userService, mapper, jwtUtil, messageSource, passwordEncoder);
    }

    @Test
    void updateProfileTest() {
        //SETUP
        UUID id = UUID.randomUUID();
        String username = "username";
        String password = "1234";
        Role role = Role.builder()
                .name(RoleUtil.ADMIN)
                .build();
        UserDto inputDto = UserDto.builder()
                .id(id)
                .username(username)
                .password(password)
                .build();
        User user = User.builder()
                .id(id)
                .username(username)
                .password(password)
                .firstName("john")
                .roles(Set.of(role))
                .build();
        UserDto outputDto = UserDto.builder()
                .id(id)
                .username(username)
                .password(password)
                .firstName("john")
                .build();
        Mockito.when(jwtUtil.getAuthenticatedUsername())
                .thenReturn(username);
        Mockito.when(userService.findUserById(id))
                .thenReturn(user);
        //TRY
        //CASE 1: User update their own profile
        LoginResultDto result = service.updateProfile(inputDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(outputDto.getUsername(), result.getUserData().getUsername());

        //CASE 2: User update another person's profile
        UserDto stranger = UserDto.builder()
                .username("stranger")
                .build();
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.updateProfile(stranger));
    }

    @Test
    void changePasswordTest() {
        //SETUP
        UUID id = UUID.randomUUID();
        String username = "username";
        String newPassword = "newPassword";
        String oldPassword = "oldPassword";
        String encodedOldPassword = passwordEncoder.encode(oldPassword);
        User user = User.builder()
                .id(id)
                .username(username)
                .password(encodedOldPassword)
                .build();
        ChangePasswordForm form = ChangePasswordForm.builder()
                .userId(id)
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .build();
        Mockito.when(userService.findUserById(id)).thenReturn(user);
        //TRY
        //CASE 1: User change their own passwords. Input Form has the correct oldPasswords
        Mockito.when(jwtUtil.getAuthenticatedUsername()).thenReturn(username);
        Assertions.assertDoesNotThrow(() -> service.changePassword(form));
        Assertions.assertTrue(passwordEncoder.matches(newPassword, user.getPassword()));
        //CASE 2: newPassword and oldPassword don't match
        user.setPassword(encodedOldPassword); //reset passwords
        form.setOldPassword("strange passwords");
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.changePassword(form));
        Assertions.assertTrue(passwordEncoder.matches(oldPassword, user.getPassword()));
        //CASE 3: User change another person's passwords
        user.setPassword(encodedOldPassword);
        Mockito.when(jwtUtil.getAuthenticatedUsername()).thenReturn("stranger");
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.changePassword(form));
        Assertions.assertTrue(passwordEncoder.matches(oldPassword, user.getPassword()));
    }
}
