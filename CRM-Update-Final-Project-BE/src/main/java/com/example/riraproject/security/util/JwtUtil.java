package com.example.riraproject.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.riraproject.common.exception.RiraAuthenticationException;
import com.example.riraproject.security.dto.RefreshTokenDto;
import com.example.riraproject.user.model.User;
import com.example.riraproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private static final String SECRET_KEY = "myLovelySecret";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
    private final UserService userService;

    public String getAccessToken(User user) {
        long accessTokenTime = 600000L; // 10 minutes
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenTime))
                .sign(algorithm);
    }

    public String getRefreshToken(User user) {
        long refreshTokenTime = 864000000L; // 10 days
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenTime))
                .sign(algorithm);
    }

    public String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        if (authentication.getPrincipal() instanceof String principal) {
            return principal;
        }
        UserDetails userDetails = (UserDetails) authentication;
        return userDetails.getUsername();
    }

    public UsernamePasswordAuthenticationToken verifyToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
    }

    public RefreshTokenDto refreshToken(String refreshToken) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refreshToken);
            String username = decodedJWT.getSubject();
            User user = userService.findByUsername(username);
            return RefreshTokenDto
                    .builder()
                    .accessToken(getAccessToken(user))
                    .refreshToken(getRefreshToken(user))
                    .build();
        } catch (Exception e) {
            throw new RiraAuthenticationException("Refresh token is invalid: " + e.getMessage());
        }
    }
}
