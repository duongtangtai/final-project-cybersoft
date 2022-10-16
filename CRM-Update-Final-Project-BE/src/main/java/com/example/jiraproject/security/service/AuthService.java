package com.example.jiraproject.security.service;

import com.example.jiraproject.security.dto.LoginFormDto;
import com.example.jiraproject.security.dto.LoginResultDto;

public interface AuthService {
    LoginResultDto login(LoginFormDto dto);
}
