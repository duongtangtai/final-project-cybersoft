package com.example.jiraproject.user.validation.validator;

import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.user.dto.UserDto;
import com.example.jiraproject.user.model.User;
import com.example.jiraproject.user.repository.UserRepository;
import com.example.jiraproject.user.validation.annotation.UniqueUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
public class UniqueUserValidator implements ConstraintValidator<UniqueUser, UserDto> {
    private final UserRepository repository;
    private String message;
    private final MessageSource messageSource;
    @Override
    public void initialize(UniqueUser constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(UserDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            buildContext(message, context);
            return false;
        }
        //if ID is null => new User => check whether username existed
        if (dto.getId() == null) {
            return isNewUserValid(dto, context);
        }
        //Otherwise check whether the username has changed
        return isOldUserValid(dto, context);
    }

    private void buildContext(String message, ConstraintValidatorContext context) {
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }

    private boolean isNewUserValid(UserDto dto, ConstraintValidatorContext context) {
        Optional<User> userOptional = repository.findByUsername(dto.getUsername());
        buildContext(message, context);
        return userOptional.isEmpty();
    }

    private boolean isOldUserValid(UserDto dto, ConstraintValidatorContext context) {
        Optional<User> userOptional = repository.findById(dto.getId());
        if (userOptional.isEmpty()) {
            message = MessageUtil.getMessage(messageSource,"user.id.not-found");
            buildContext(message, context);
            return false;
        }
        //if oldUsername doesn't equals to newUsername, check whether the newUsername valid
        User user = userOptional.get();
        return user.getUsername().equals(dto.getUsername()) || isNewUserValid(dto, context);
    }
}
