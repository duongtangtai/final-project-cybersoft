package com.example.jiraproject.security.filter;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.DateTimeUtil;
import com.example.jiraproject.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomOncePerRequestFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public CustomOncePerRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/login")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationToken = request.getHeader(AUTHORIZATION);
            if (authorizationToken != null && authorizationToken.startsWith("Bearer ")) {
                String token = authorizationToken.substring("Bearer ".length());
                log.info("Trying to verify token: {}", token);
                try {
                    UsernamePasswordAuthenticationToken authenticationToken = jwtUtil.verifyToken(token);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                    log.info("Token is valid");
                } catch (Exception e) {
                    log.error("Token is invalid: " + e.getMessage());
                    response.setStatus(UNAUTHORIZED.value());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),
                            ResponseDto.builder()
                                    .content(null)
                                    .hasErrors(true)
                                    .errors(List.of(e.getMessage()))
                                    .timeStamp(DateTimeUtil.now())
                                    .statusCode(401)
                                .build());
                }
            } else {
//                filterChain.doFilter(request, response);
                response.setStatus(UNAUTHORIZED.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        ResponseDto.builder()
                                .content(null)
                                .hasErrors(true)
                                .errors(List.of("Bạn cần phải đăng nhập"))
                                .timeStamp(DateTimeUtil.now())
                                .statusCode(401)
                                .build());
            }
        }
    }
}
