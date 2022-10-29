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
import javax.validation.ValidationException;
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
    public boolean isValid(UserDto dto, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        if (dto == null) {
            return false;
        }
        //if ID is null => new User => check whether username existed
        if (dto.getId() == null) {
            return isNewUserValid(dto);
        }
        //Otherwise check whether the username has changed
        return isOldUserValid(dto);
    }

    private boolean isNewUserValid(UserDto dto) {
        Optional<User> userOptional = repository.findByUsername(dto.getUsername());
        return userOptional.isEmpty();
    }

    private boolean isOldUserValid(UserDto dto) {
        User user = repository.findById(dto.getId())
                .orElseThrow(() -> new ValidationException(
                        MessageUtil.getMessage(messageSource, "user.id.not-found")));
        //if oldUsername doesn't equals to newUsername, check whether the newUsername valid
        return user.getUsername().equals(dto.getUsername()) || isNewUserValid(dto);
    }
}
