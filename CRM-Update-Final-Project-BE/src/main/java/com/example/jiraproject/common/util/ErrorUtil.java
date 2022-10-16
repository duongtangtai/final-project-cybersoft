package com.example.jiraproject.common.util;

import com.example.jiraproject.notification.model.Notification;
import com.example.jiraproject.operation.model.Operation;
import com.example.jiraproject.task.model.Task;
import com.example.jiraproject.user.model.User;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.experimental.UtilityClass;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class ErrorUtil {
    public static List<String> getErrorMessage(ValidationException exception) {
        return List.of(exception.getMessage());
    }
    public static List<String> getErrorMessage(MethodArgumentNotValidException exception) {
        return exception.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
    }

    public static List<String> getErrorMessage(InvalidFormatException exception) {
        Class<?> targetType = exception.getTargetType();
        if (UUID.class.equals(targetType)) {
            return List.of(MessageUtil.INVALID_UUID_FORMAT);
        } else if (LocalDate.class.equals(targetType)){
            return List.of(MessageUtil.INVALID_DATE_FORMAT);
        } else if (Operation.Type.class.equals(targetType)) {
            return List.of(MessageUtil.INVALID_OPERATION_TYPE);
        } else if (User.AccountStatus.class.equals(targetType)){
            return List.of(MessageUtil.INVALID_USER_ACCOUNT_STATUS);
        } else if (Task.Status.class.equals(targetType)){
            return List.of(MessageUtil.INVALID_TASK_STATUS);
        } else if (Notification.Status.class.equals(targetType)){
            return List.of(MessageUtil.INVALID_NOTIFICATION_STATUS);
        } else {
            return List.of(exception.getOriginalMessage());
        }
    }

    //ConstraintViolationException is a child of ValidationException -> it catches exception before its parents
    public static List<String> getErrorMessage(ConstraintViolationException exception) {
        return exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
    }
}
