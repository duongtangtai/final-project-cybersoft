package com.example.riraproject.security.service;

import com.example.riraproject.common.exception.RiraAuthenticationException;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.role.model.Role;
import com.example.riraproject.security.dto.LoginFormDto;
import com.example.riraproject.security.dto.LoginResultDto;
import com.example.riraproject.security.dto.RefreshTokenDto;
import com.example.riraproject.security.util.JwtUtil;
import com.example.riraproject.user.dto.UserDto;
import com.example.riraproject.user.model.User;
import com.example.riraproject.user.repository.UserRepository;
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
                        new RiraAuthenticationException(
                                MessageUtil.getMessage(messageSource, "account.username.not-found"))); // throw exception if cannot find username
        String status = user.getAccountStatus().toString();
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) { // if password is incorrect
            throw new RiraAuthenticationException(
                    MessageUtil.getMessage(messageSource, "account.password.incorrect"));
        } else if (!status.equals("ACTIVE")) {
            //if password is correct -> Check accountStatus to make sure it's ACTIVE, otherwise block user
            throw new RiraAuthenticationException(
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
