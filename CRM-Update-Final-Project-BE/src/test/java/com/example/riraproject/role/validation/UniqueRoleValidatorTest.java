package com.example.riraproject.role.validation;

import com.example.riraproject.role.dto.RoleDto;
import com.example.riraproject.role.model.Role;
import com.example.riraproject.role.repository.RoleRepository;
import com.example.riraproject.role.validation.annotation.UniqueRole;
import com.example.riraproject.role.validation.validator.UniqueRoleValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UniqueRoleValidatorTest {
    @Mock private RoleRepository repository;
    private UniqueRoleValidator validator;

    @BeforeEach
    void init() {
        validator = new UniqueRoleValidator(repository);
    }

    @Test
    void validatorShouldWork() {
        //SETUP
        String message = "some message";
        UniqueRole constraint = Mockito.mock(UniqueRole.class);
        Mockito.when(constraint.message()).thenReturn(message);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder =
                Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext newContext = Mockito.mock(ConstraintValidatorContext.class);
        Mockito.when(context.buildConstraintViolationWithTemplate(message))
                .thenReturn(builder);
        Mockito.when(builder.addConstraintViolation())
                .thenReturn(newContext);
        Assertions.assertDoesNotThrow(() -> validator.initialize(constraint));
        //TRY
        //CASE 1: roleDto is null
        Assertions.assertFalse(validator.isValid(null, context));
        //CASE 2: roleDto is not null, code or name valid
        Role role = Role.builder()
                .name("ADMIN")
                .code("AD")
                .build();
        RoleDto roleDto = RoleDto.builder()
                .name("ADMIN")
                .code("AD")
                .build();
        Mockito.when(repository.findByNameOrCode(roleDto.getName(), roleDto.getCode()))
                .thenReturn(Optional.empty());
        Assertions.assertTrue(validator.isValid(roleDto, context));
        Mockito.verify(context).buildConstraintViolationWithTemplate(captor.capture());
        Assertions.assertEquals(message, captor.getValue());
        //CASE 2: roleDto is not null, code or name invalid
        Mockito.when(repository.findByNameOrCode(roleDto.getName(), roleDto.getCode()))
                .thenReturn(Optional.of(role));
        Assertions.assertFalse(validator.isValid(roleDto, context));
    }
}
