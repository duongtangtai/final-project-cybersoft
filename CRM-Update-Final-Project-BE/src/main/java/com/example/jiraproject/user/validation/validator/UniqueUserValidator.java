package com.example.jiraproject.user.validation.validator;

import com.example.jiraproject.user.dto.UserDto;
import com.example.jiraproject.user.model.User;
import com.example.jiraproject.user.repository.UserRepository;
import com.example.jiraproject.user.validation.annotation.UniqueUser;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
public class UniqueUserValidator implements ConstraintValidator<UniqueUser, UserDto> {
    private final UserRepository repository;
    private String message;
    @Override
    public void initialize(UniqueUser constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(UserDto userDto, ConstraintValidatorContext constraintValidatorContext) {
        if (userDto == null) {
            return false;
        }
        constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        Optional<User> userOptional = repository.findByUsernameOrEmail(userDto.getUsername(), userDto.getEmail());
        return userOptional.isEmpty();
    }
}
