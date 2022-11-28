package com.example.riraproject.security.service;

import com.example.riraproject.common.exception.RiraAuthenticationException;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.notification.service.NotificationService;
import com.example.riraproject.role.model.Role;
import com.example.riraproject.security.dto.LoginFormDto;
import com.example.riraproject.security.dto.LoginResultDto;
import com.example.riraproject.security.dto.RefreshTokenDto;
import com.example.riraproject.security.util.JwtUtil;
import com.example.riraproject.user.dto.UserDto;
import com.example.riraproject.user.model.User;
import com.example.riraproject.user.repository.UserRepository;
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

import java.util.Optional;
import java.util.Set;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private NotificationService notificationService;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Autowired private ModelMapper mapper;
    @Autowired private MessageSource messageSource;
    private AuthServiceImpl service;

    @BeforeEach
    void init() {
        service = new AuthServiceImpl(userRepository, notificationService, passwordEncoder,jwtUtil,
                mapper, messageSource);
    }

    @Test
    void loginTest() {
        //SETUP
        String username = "username";
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .accountStatus(User.AccountStatus.TEMPORARILY_BLOCKED)
                .roles(Set.of(Role.builder().code("EMP").build()))
                .build();
        LoginFormDto loginFormDto = new LoginFormDto();
        loginFormDto.setUsername(username);
        loginFormDto.setPassword(password);
        //CASE 1: Username is wrong
        Mockito.when(userRepository.findByUsername(loginFormDto.getUsername()))
                .thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(RiraAuthenticationException.class,
                () -> service.login(loginFormDto));
        //CASE 2: Username is correct, password is wrong
        Mockito.when(userRepository.findByUsername(loginFormDto.getUsername()))
                .thenReturn(Optional.of(user));
        loginFormDto.setPassword("invalid password");
        Assertions.assertThrowsExactly(RiraAuthenticationException.class,
                () -> service.login(loginFormDto));
        //CASE 3: Username is correct, password is correct, status is blocked
        loginFormDto.setPassword(password);
        Assertions.assertThrowsExactly(RiraAuthenticationException.class,
                () -> service.login(loginFormDto));
        //CASE 4: Username is correct, password is correct, status is active
        //if this account is logged in somewhere => send log out event
        //otherwise, simply login
        user.setAccountStatus(User.AccountStatus.ACTIVE);
        //account logged in somewhere
        Mockito.when(notificationService.isSubscriber(user.getUsername()))
                .thenReturn(true);
        LoginResultDto result = service.login(loginFormDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getUsername(), result.getUserData().getUsername());
        Assertions.assertEquals(user.getAccountStatus(), result.getUserData().getAccountStatus());
        Mockito.verify(notificationService).sendLogoutEvent(user.getUsername(),
                MessageUtil.getMessage(messageSource,
                        "notification.login-from-another-device"));
    }

    @Test
    void refreshTokenTest() {
        String refreshToken = "refreshToken";
        RefreshTokenDto refreshTokenDto = Mockito.mock(RefreshTokenDto.class);
        Mockito.when(jwtUtil.refreshToken(refreshToken)).thenReturn(refreshTokenDto);
        Assertions.assertEquals(refreshTokenDto, service.refreshToken(refreshToken));
    }
}
