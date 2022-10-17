package com.example.jiraproject.security.aop;


import com.example.jiraproject.operation.model.Operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authorized {
    String operation() default "";
    Operation.Type type() default Operation.Type.FETCH;
}
