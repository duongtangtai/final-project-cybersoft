package com.example.riraproject.security.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginFormDto {
    @Size(min = 5, max = 25, message = "{user.username.size}")
    @NotBlank(message = "{user.username.not-blank}")
    private String username;

    @Size(min = 5, max = 300, message = "{user.password.size}")
    @NotBlank(message = "{user.password.not-blank}")
    private String password;
}
