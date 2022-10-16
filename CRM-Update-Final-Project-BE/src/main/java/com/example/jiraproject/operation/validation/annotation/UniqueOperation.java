package com.example.jiraproject.operation.validation.annotation;

import com.example.jiraproject.operation.validation.validator.UniqueOperationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueOperationValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UniqueOperation {
    String message() default "{operation.existed}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
