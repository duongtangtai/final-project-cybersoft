package com.example.riraproject.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.riraproject.common.exception.RiraAuthenticationException;
import com.example.riraproject.security.dto.RefreshTokenDto;
import com.example.riraproject.user.model.User;
import com.example.riraproject.user.service.UserService;
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
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class JwtUtilTest {
    @Mock private UserService userService;
    private JwtUtil jwtUtil;
    private final String SECRET_KEY = "myLovelySecret";
    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());

    @BeforeEach
    void init() {
        jwtUtil = new JwtUtil(userService);
    }

    @Test
    void getAccessTokenTest() {
        //SETUP
        User user = User.builder()
                .username("someone")
                .build();
        String token = jwtUtil.getAccessToken(user);
        //TRY
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        Assertions.assertEquals("someone", username);
    }

    @Test
    void getRefreshTokenTest() {
        //SETUP
        User user = User.builder()
                .username("someone")
                .build();
        String token = jwtUtil.getRefreshToken(user);
        //TRY
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        Assertions.assertEquals("someone", username);
    }

    @Test
    void getAuthenticatedUsernameTest() {
        //SETUP
        //SET AUTHENTICATION
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken("username",
                        null, Collections.emptyList());
        //CASE 1: AUTHENTICATION IS NULL
        SecurityContextHolder.getContext().setAuthentication(null);
        Assertions.assertNull(jwtUtil.getAuthenticatedUsername());
        //CASE 2: AUTHENTICATION IS NOT NULL and IS String
        SecurityContextHolder.getContext().setAuthentication(token);
        Assertions.assertEquals("username", jwtUtil.getAuthenticatedUsername());
        //CASE 3: AUTHENTICATION IS NOT NULL and IS UserDetail
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("username");
        UsernamePasswordAuthenticationToken token2 =
                new UsernamePasswordAuthenticationToken(userDetails,
                        null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(token2);
        Assertions.assertEquals("username", jwtUtil.getAuthenticatedUsername());
    }

    @Test
    void verifyTokenTest() {
        //SETUP
        //create token
        User user = User.builder()
                .username("username")
                .build();
        String accessToken = jwtUtil.getAccessToken(user);
        //TRY
        UsernamePasswordAuthenticationToken authenticationToken= jwtUtil.verifyToken(accessToken);
        Assertions.assertEquals("username", authenticationToken.getPrincipal());
        Assertions.assertNull(authenticationToken.getCredentials());
        Assertions.assertEquals(Collections.emptyList(), authenticationToken.getAuthorities());
    }

    @Test
    void refreshTokenTest() {
        //SETUP
        //create token
        User user = User.builder()
                .username("username")
                .build();
        String refreshToken = jwtUtil.getRefreshToken(user);
        Mockito.when(userService.findByUsername("username")).thenReturn(user);

        //TRY
        //CASE 1: REFRESH TOKEN IS VALID
        RefreshTokenDto result = jwtUtil.refreshToken(refreshToken);
        Assertions.assertNotNull(result);
        String newAccessToken = result.getAccessToken();
        String newRefreshToken = result.getRefreshToken();
        Assertions.assertEquals("username", jwtUtil.verifyToken(newAccessToken).getPrincipal());
        Assertions.assertEquals("username", jwtUtil.verifyToken(newRefreshToken).getPrincipal());
        Mockito.verify(userService).findByUsername("username");

        //CASE 2: REFRESH TOKEN IS INVALID
        String invalidRefreshToken = "this is odd";
        Assertions.assertThrowsExactly(RiraAuthenticationException.class,
                () -> jwtUtil.refreshToken(invalidRefreshToken));
    }
}
