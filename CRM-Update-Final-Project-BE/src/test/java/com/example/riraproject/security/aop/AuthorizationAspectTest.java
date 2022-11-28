package com.example.riraproject.security.aop;

import com.example.riraproject.common.exception.RiraAuthorizationException;
import com.example.riraproject.role.model.Role;
import com.example.riraproject.role.repository.RoleRepository;
import com.example.riraproject.security.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthorizationAspectTest {
    @Mock private JwtUtil jwtUtil;
    @Mock private RoleRepository roleRepository;
    @Autowired private MessageSource messageSource;
    private AuthorizationAspect aspect;

    @BeforeEach
    void init() {
        aspect = new AuthorizationAspect(jwtUtil, roleRepository, messageSource);
    }

    @Test
    void authorizationTest() {
        //SETUP
        Authorized constraint = Mockito.mock(Authorized.class);
        String[] requiredRoles = new String[] {"AD", "MNG"};
        Mockito.when(constraint.roles()).thenReturn(requiredRoles);
        String username = "user";
        Mockito.when(jwtUtil.getAuthenticatedUsername()).thenReturn(username);
        //CASE 1: User doesn't have any roles
        Mockito.when(roleRepository.findByUsername(username))
                .thenReturn(Collections.emptyList());
        Assertions.assertThrowsExactly(RiraAuthorizationException.class,
                () -> aspect.authorization(constraint));
        Mockito.verify(roleRepository).findByUsername(username);
        //CASE 2: User has roles but not one of those required roles
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(Role.builder().code("EMP").build());
        Mockito.when(roleRepository.findByUsername(username))
                .thenReturn(userRoles);
        Assertions.assertThrowsExactly(RiraAuthorizationException.class,
                () -> aspect.authorization(constraint));
        //CASE 2: User has roles which contains one of those required roles
        userRoles.add(Role.builder().code("MNG").build());
        Assertions.assertDoesNotThrow(() -> aspect.authorization(constraint));
    }
}
