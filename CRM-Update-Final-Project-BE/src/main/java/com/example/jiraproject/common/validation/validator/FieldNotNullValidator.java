package com.example.jiraproject.common.validation.validator;

import com.example.jiraproject.common.validation.annotation.FieldNotNull;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class FieldNotNullValidator implements ConstraintValidator<FieldNotNull, Object> {
    private String message;

    /**
     * Handle all the types here -> to initialize the messages
     */
    @Override
    public void initialize(FieldNotNull constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object type, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return type != null;
    }
}
