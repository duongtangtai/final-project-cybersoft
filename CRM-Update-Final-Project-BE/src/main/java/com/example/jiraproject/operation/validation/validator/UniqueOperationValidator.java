package com.example.jiraproject.operation.validation.validator;

import com.example.jiraproject.operation.dto.OperationDto;
import com.example.jiraproject.operation.model.Operation;
import com.example.jiraproject.operation.repository.OperationRepository;
import com.example.jiraproject.operation.validation.annotation.UniqueOperation;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
public class UniqueOperationValidator implements ConstraintValidator<UniqueOperation, OperationDto> {
    private final OperationRepository repository;
    private String message;
    @Override
    public void initialize(UniqueOperation constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(OperationDto operationDto, ConstraintValidatorContext constraintValidatorContext) {
        if (operationDto == null) {
            return false;
        }
        constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        Optional<Operation> operationOptional =
                repository.findByNameAndType(operationDto.getName(), operationDto.getType());
        return operationOptional.isEmpty();
    }
}
