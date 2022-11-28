package com.example.riraproject.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.riraproject.security.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomOncePerRequestFilterTest {
    @Mock private JwtUtil jwtUtil;
    private CustomOncePerRequestFilter filter;

    @BeforeEach
    void init() {
        filter = new CustomOncePerRequestFilter(jwtUtil);
    }

    @Test
    void filterShouldWork() throws IOException {
        //SETUP
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        ServletOutputStream outputStream = Mockito.mock(ServletOutputStream.class);
        Mockito.when(response.getOutputStream()).thenReturn(outputStream);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        String AUTHORIZATION = "Authorization";
        //CASE 1: Request doesn't have Authorization Token
        Mockito.when(request.getHeader(AUTHORIZATION))
                .thenReturn(null);
        Assertions.assertDoesNotThrow(() -> filter.doFilterInternal(request, response, filterChain));
        Mockito.verify(jwtUtil, Mockito.never()).verifyToken(null);
        //CASE 2: Request have Authorization Token but token doesn't start with "Bearer "
        String token;
        token = "invalid token";
        Mockito.when(request.getHeader(AUTHORIZATION))
                .thenReturn(token);
        Assertions.assertDoesNotThrow(() -> filter.doFilterInternal(request, response, filterChain));
        Mockito.verify(jwtUtil, Mockito.never()).verifyToken(token);
        //CASE 3: Request have Authorization Token, token start with "Bearer ", but invalid
        JWTVerificationException exception = Mockito.mock(JWTVerificationException.class);
        token = "Bearer invalid";
        Mockito.when(request.getHeader(AUTHORIZATION))
                .thenReturn(token);
        Mockito.when(jwtUtil.verifyToken(token.substring("Bearer ".length())))
                .thenThrow(exception);
        Mockito.when(exception.getMessage()).thenReturn("error while verifying token");
        Assertions.assertDoesNotThrow(() -> filter.doFilterInternal(request, response, filterChain));
        Mockito.verify(jwtUtil).verifyToken(token.substring("Bearer ".length()));
        Mockito.verify(response).getOutputStream();
        //CASE 4: Request have Authorization Token, token start with "Bearer " and valid
        token = "Bearer valid";
        Mockito.when(request.getHeader(AUTHORIZATION))
                .thenReturn(token);
        UsernamePasswordAuthenticationToken authenticationToken =
                Mockito.mock(UsernamePasswordAuthenticationToken.class);
        Mockito.when(jwtUtil.verifyToken(token.substring("Bearer ".length())))
                .thenReturn(authenticationToken);
        Assertions.assertDoesNotThrow(() -> filter.doFilterInternal(request, response, filterChain));
        Mockito.verify(jwtUtil).verifyToken(token.substring("Bearer ".length()));
        Assertions.assertEquals(authenticationToken, SecurityContextHolder.getContext().getAuthentication());
    }
}
