package com.example.jiraproject.common.validation.validator;

import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.common.validation.annotation.FieldNotNull;
import com.example.jiraproject.notification.model.Notification;
import com.example.jiraproject.operation.model.Operation;
import com.example.jiraproject.task.model.Task;
import com.example.jiraproject.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class FieldNotNullValidator implements ConstraintValidator<FieldNotNull, Object> {
    private final MessageSource messageSource;
    private String message;

    /**
     * Handle all the types here -> to initialize the messages
     */
    @Override
    public void initialize(FieldNotNull constraintAnnotation) {
        Class<?> target = constraintAnnotation.target();
        if (Operation.Type.class.equals(target)) {
            message = MessageUtil.getMessage(messageSource, "operation.type.not-null");
        } else if (User.AccountStatus.class.equals(target)) {
            message = MessageUtil.getMessage(messageSource, "user.accountStatus.not-null");
        } else if (Task.Status.class.equals(target)) {
            message = MessageUtil.getMessage(messageSource, "task.status.not-null");
        } else if (Notification.Status.class.equals(target)) {
            message = MessageUtil.getMessage(messageSource, "notification.status.not-null");
        } else {
            message = constraintAnnotation.message();
        }
    }

    @Override
    public boolean isValid(Object type, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return type != null;
    }
}
