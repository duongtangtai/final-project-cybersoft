package com.example.jiraproject.role.validation.validator;

import com.example.jiraproject.role.dto.RoleDto;
import com.example.jiraproject.role.model.Role;
import com.example.jiraproject.role.repository.RoleRepository;
import com.example.jiraproject.role.validation.annotation.UniqueRole;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
public class UniqueRoleValidator implements ConstraintValidator<UniqueRole, RoleDto> {
    private final RoleRepository repository;
    private String message;
    @Override
    public void initialize(UniqueRole constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(RoleDto roleDto, ConstraintValidatorContext constraintValidatorContext) {
        if (roleDto == null) {
            return false;
        }
        constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        Optional<Role> roleOptional = repository.findByNameOrCode(roleDto.getName(), roleDto.getCode());
        return roleOptional.isEmpty();
    }

}
