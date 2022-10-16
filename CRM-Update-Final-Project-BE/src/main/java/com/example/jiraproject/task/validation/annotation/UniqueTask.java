package com.example.jiraproject.task.validation.annotation;

import com.example.jiraproject.task.validation.validator.UniqueTaskValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueTaskValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UniqueTask {
    String message() default "{task.existed}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
