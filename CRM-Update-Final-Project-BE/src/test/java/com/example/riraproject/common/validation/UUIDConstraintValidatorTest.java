package com.example.riraproject.common.validation;

import com.example.riraproject.common.validation.annotation.UUIDConstraint;
import com.example.riraproject.common.validation.validator.UUIDConstraintValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UUIDConstraintValidatorTest {

    @Mock UUIDConstraint constraint;
    private UUIDConstraintValidator validator;

    @BeforeEach
    void init() {
        validator = new UUIDConstraintValidator();
    }

    @Test
    void validatorShouldWork() {
        //SETUP
        String message = "some message";
        Mockito.when(constraint.message()).thenReturn(message);
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext newContext = Mockito.mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder =
                Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        Mockito.when(context.buildConstraintViolationWithTemplate(message))
                .thenReturn(builder);
        Mockito.when(builder.addConstraintViolation())
                .thenReturn(newContext);
        Mockito.doNothing().when(newContext).disableDefaultConstraintViolation();
        //TRY
        validator.initialize(constraint);
        //CASE 1: OBJECT IS NULL
        Assertions.assertFalse(validator.isValid(null, context));
        //CASE 2: OBJECT IS UUID
        Assertions.assertTrue(validator.isValid(UUID.randomUUID(), context));
        //CASE 3: SOME OTHER CLASS
        Assertions.assertFalse(validator.isValid("some string", context));
        Assertions.assertFalse(validator.isValid(123, context));
        Assertions.assertFalse(validator.isValid('a', context));
    }
}
