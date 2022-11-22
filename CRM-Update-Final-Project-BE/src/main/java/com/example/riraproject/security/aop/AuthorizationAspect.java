package com.example.riraproject.security.aop;

import com.example.riraproject.common.exception.RiraAuthorizationException;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.role.model.Role;
import com.example.riraproject.role.repository.RoleRepository;
import com.example.riraproject.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationAspect {
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;
    private final MessageSource messageSource;

    @Before("@annotation(authorized)")
    public void authorization(Authorized authorized) {
        String[] requiredRoles = authorized.roles();
        // If we use roles in the token, we can't control it immediately right after some of the user's roles
        // are removed. Because the token is only new when it's refreshed
        List<String> userRoles = roleRepository.findByUsername(jwtUtil.getAuthenticatedUsername())
                .stream().map(Role::getCode).toList();
        if (userRoles.isEmpty()) { //this user has no roles
            throw new RiraAuthorizationException(MessageUtil.getMessage(messageSource, "unauthorized"));
        }
        //requiredRoles must exists in userRoles
        boolean isValid = Arrays.stream(requiredRoles)
                .anyMatch(requiredRole -> userRoles.stream()
                        .anyMatch(requiredRole::equals));
        if (!isValid) {
            throw new RiraAuthorizationException(MessageUtil.getMessage(messageSource, "unauthorized"));
        }
    }
}
