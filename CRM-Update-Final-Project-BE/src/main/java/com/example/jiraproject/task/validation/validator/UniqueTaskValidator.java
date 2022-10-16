package com.example.jiraproject.task.validation.validator;

import com.example.jiraproject.task.dto.TaskDto;
import com.example.jiraproject.task.model.Task;
import com.example.jiraproject.task.repository.TaskRepository;
import com.example.jiraproject.task.validation.annotation.UniqueTask;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
public class UniqueTaskValidator implements ConstraintValidator<UniqueTask, TaskDto> {
    private final TaskRepository repository;
    private String message;
    @Override
    public void initialize(UniqueTask constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(TaskDto taskDto, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        Optional<Task> taskOptional = repository.findByName(taskDto.getName());
        return taskOptional.isEmpty();
    }
}
