package com.example.jiraproject.security.service;

import com.example.jiraproject.common.exception.JiraAuthenticationException;
import com.example.jiraproject.role.model.Role;
import com.example.jiraproject.security.dto.LoginFormDto;
import com.example.jiraproject.security.dto.LoginResultDto;
import com.example.jiraproject.security.util.JwtUtil;
import com.example.jiraproject.user.model.User;
import com.example.jiraproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResultDto login(LoginFormDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() ->
                        new JiraAuthenticationException("Không tìm thấy username trùng khớp")); // throw exception if cannot find username
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) { // if password is incorrect
            throw new JiraAuthenticationException("Mật khẩu không chính xác");
        } else {
            return LoginResultDto.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .roleCodes(user.getRoles().stream().map(Role::getCode).toList())
                    .accessToken(jwtUtil.getAccessToken(user))
                    .refreshToken(jwtUtil.getRefreshToken(user))
                    .build();
        }
    }
}