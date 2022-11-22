package com.example.riraproject.security.dto;

import com.example.riraproject.user.dto.UserDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResultDto {
    private String accessToken;
    private String refreshToken;
    private UserDto userData;
    private List<String> roleCodes;
}