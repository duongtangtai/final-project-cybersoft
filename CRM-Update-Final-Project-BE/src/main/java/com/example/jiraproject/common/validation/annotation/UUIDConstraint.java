package com.example.jiraproject.common.validation.annotation;

import com.example.jiraproject.common.validation.validator.UUIDConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UUIDConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface UUIDConstraint {
    String message() default "{id.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
