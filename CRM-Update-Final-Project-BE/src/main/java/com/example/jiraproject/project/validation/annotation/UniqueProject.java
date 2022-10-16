package com.example.jiraproject.project.validation.annotation;

import com.example.jiraproject.project.validation.validator.UniqueProjectValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueProjectValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UniqueProject {
    String message() default "{project.existed}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
