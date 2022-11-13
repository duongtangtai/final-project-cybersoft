package com.example.jiraproject.security.service;

import com.example.jiraproject.common.exception.JiraAuthenticationException;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.role.model.Role;
import com.example.jiraproject.security.dto.LoginFormDto;
import com.example.jiraproject.security.dto.LoginResultDto;
import com.example.jiraproject.security.dto.RefreshTokenDto;
import com.example.jiraproject.security.util.JwtUtil;
import com.example.jiraproject.user.dto.UserDto;
import com.example.jiraproject.user.model.User;
import com.example.jiraproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

public interface AuthService {
    LoginResultDto login(LoginFormDto dto);
    RefreshTokenDto refreshToken(String refreshToken);
}

@Service
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ModelMapper mapper;
    private final MessageSource messageSource;

    @Override
    public LoginResultDto login(LoginFormDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() ->
                        new JiraAuthenticationException(
                                MessageUtil.getMessage(messageSource, "account.username.not-found"))); // throw exception if cannot find username
        String status = user.getAccountStatus().toString();
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) { // if password is incorrect
            throw new JiraAuthenticationException(
                    MessageUtil.getMessage(messageSource, "account.password.incorrect"));
        } else if (!status.equals("ACTIVE")) {
            //if password is correct -> Check accountStatus to make sure it's ACTIVE, otherwise block user
            throw new JiraAuthenticationException(
                    MessageUtil.getMessage(messageSource, "account.blocked"));
        } else {
            UserDto userDto = mapper.map(user, UserDto.class);
            userDto.setPassword(null);
            return LoginResultDto.builder()
                    .userData(userDto)
                    .roleCodes(user.getRoles().stream().map(Role::getCode).toList())
                    .accessToken(jwtUtil.getAccessToken(user))
                    .refreshToken(jwtUtil.getRefreshToken(user))
                    .build();
        }
    }

    @Override
    public RefreshTokenDto refreshToken(String refreshToken) {
        return jwtUtil.refreshToken(refreshToken);
    }
}
