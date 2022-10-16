package com.example.jiraproject.common.validation.validator;

import com.example.jiraproject.common.validation.annotation.UUIDConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

/**
 * This validator verify UUID value or UUID String
 */
public class UUIDConstraintValidator implements ConstraintValidator <UUIDConstraint, Object> {
    private String message;
    @Override
    public void initialize(UUIDConstraint constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object uuid, ConstraintValidatorContext constraintValidatorContext) {
        if (uuid == null) {
            return false;
        }
        if (uuid.getClass() == String.class || uuid.getClass() == UUID.class) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            String regex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
            return uuid.toString().matches(regex);
        }
        return false;
    }
}
