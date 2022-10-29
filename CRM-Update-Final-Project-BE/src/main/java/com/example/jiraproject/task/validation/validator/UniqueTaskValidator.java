package com.example.jiraproject.task.validation.validator;

import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.task.dto.TaskDto;
import com.example.jiraproject.task.model.Task;
import com.example.jiraproject.task.repository.TaskRepository;
import com.example.jiraproject.task.validation.annotation.UniqueTask;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.util.Optional;

@RequiredArgsConstructor
public class UniqueTaskValidator implements ConstraintValidator<UniqueTask, TaskDto> {
    private final TaskRepository repository;
    private String message;
    private final MessageSource messageSource;
    @Override
    public void initialize(UniqueTask constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(TaskDto dto, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        if (dto == null) {
            return false;
        }
        if (dto.getId() == null) {
            return isNewTaskValid(dto);
        }
        return isOldTaskValid(dto);
    }

    private boolean isNewTaskValid(TaskDto dto) { //Inside one project, tasks can't have the same name
        Optional<Task> taskOptional = repository.findByNameAndProjectName(dto.getName(), dto.getProjectName());
        return taskOptional.isEmpty();
    }

    private boolean isOldTaskValid(TaskDto dto) {
        Task task = repository.findById(dto.getId())
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, "task.id.not-found")));
        return task.getName().equals(dto.getName()) && task.getProject().getName().equals(dto.getProjectName())
                || isNewTaskValid(dto);
    }
}
