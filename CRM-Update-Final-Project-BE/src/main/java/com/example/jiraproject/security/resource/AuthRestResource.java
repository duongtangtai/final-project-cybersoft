package com.example.jiraproject.security.resource;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.ResponseUtil;
import com.example.jiraproject.security.dto.LoginFormDto;
import com.example.jiraproject.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthRestResource {
    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody @Valid LoginFormDto dto) {
        return ResponseUtil.get(service.login(dto), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseDto> refreshToken(@RequestParam("refreshToken") @NotBlank String refreshToken) {
        return ResponseUtil.get(service.refreshToken(refreshToken), HttpStatus.OK);
    }
}