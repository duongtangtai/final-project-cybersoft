package com.example.jiraproject.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "myLovelySecret";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
    public String getAccessToken(UserDetails user, HttpServletRequest request) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer(request.getRequestURL().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withClaim("roles", user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .sign(algorithm);
    }
    public String getRefreshToken(UserDetails user, HttpServletRequest request) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer(request.getRequestURL().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .sign(algorithm);
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
}
