package com.example.riraproject.security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {
    private String accessToken;
    private String refreshToken;
}
