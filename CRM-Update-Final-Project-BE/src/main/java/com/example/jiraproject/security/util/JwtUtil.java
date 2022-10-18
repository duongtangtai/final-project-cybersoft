package com.example.jiraproject.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.jiraproject.common.exception.JiraAuthenticationException;
import com.example.jiraproject.role.model.Role;
import com.example.jiraproject.security.dto.RefreshTokenDto;
import com.example.jiraproject.user.model.User;
import com.example.jiraproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private static final String SECRET_KEY = "myLovelySecret";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
    private final UserService userService;

    public String getAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withClaim("roles", user.getRoles()
                        .stream()
                        .map(Role::getCode)
                        .toList())
                .sign(algorithm);
    }

    public String getRefreshToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
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
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
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
            throw new JiraAuthenticationException("Refresh token is invalid: " + e.getMessage());
        }
    }
}
