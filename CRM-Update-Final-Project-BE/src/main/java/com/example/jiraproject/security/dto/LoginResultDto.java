package com.example.jiraproject.security.dto;

import com.example.jiraproject.user.dto.UserDto;
import lombok.*;

import java.util.List;
import java.util.UUID;

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