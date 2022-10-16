package com.example.jiraproject.security.filter;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.DateTimeUtil;
import com.example.jiraproject.role.model.Role;
import com.example.jiraproject.security.util.JwtUtil;
import com.example.jiraproject.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final DaoAuthenticationProvider provider;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    public CustomAuthenticationFilter(DaoAuthenticationProvider provider,  UserService userService, JwtUtil jwtUtil) {
        this.provider = provider;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        return provider.authenticate(authenticationToken); //may throw InternalAuthenticationServiceException
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        log.info("SUCCESSFULLY AUTHENTICATED");
        User user = (User) authentication.getPrincipal();
        //get tokens
        String accessToken = jwtUtil.getAccessToken(user, request);
        String refreshToken = jwtUtil.getRefreshToken(user, request);
        //get userModel to fetch info (because userDetails only have username, password and authorities
        com.example.jiraproject.user.model.User userModel =
                userService.findByUsername(user.getUsername());
        //return LOGIN RESULT to user
        Map<String, Object> loginResult = new HashMap<>();
        loginResult.put("access_token", accessToken);
        loginResult.put("refresh_token", refreshToken);
        loginResult.put("username", userModel.getUsername());
        loginResult.put("email", userModel.getEmail());
        loginResult.put("roles", userModel.getRoles().stream().map(Role::getName).toList());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(200);
        ResponseDto responseDto =ResponseDto.builder()
                .content(loginResult)
                .hasErrors(false)
                .errors(Collections.emptyList())
                .timeStamp(DateTimeUtil.now())
                .statusCode(200)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), responseDto);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(401);
        response.setContentType(APPLICATION_JSON_VALUE);
        log.error(failed.getMessage());
        ResponseDto responseDto = ResponseDto.builder()
                .content("Tài khoản hoặc mật khẩu không chính xác")
                .hasErrors(false)
                .errors(Collections.emptyList())
                .timeStamp(DateTimeUtil.now())
                .statusCode(401)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), responseDto);
    }
}
