package com.example.riraproject.common.validation;

import com.example.riraproject.common.validation.annotation.FieldNotNull;
import com.example.riraproject.common.validation.validator.FieldNotNullValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

@ExtendWith(MockitoExtension.class)
class FieldNotNullValidatorTest {

    private final String message = "some message";

    @InjectMocks
    FieldNotNullValidator validator;

    @Test
    void validatorShouldWork() {
        //SETUP
        FieldNotNull constraint = Mockito.mock(FieldNotNull.class);
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
        //CASE 1: Object is not null
        Assertions.assertTrue(validator.isValid(new Object(), context));
        //CASE 2: Object is null
        Assertions.assertFalse(validator.isValid(null, context));
    }
}