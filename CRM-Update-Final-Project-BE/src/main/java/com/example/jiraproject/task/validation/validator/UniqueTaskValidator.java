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
    public boolean isValid(TaskDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            buildContext(message, context);
            return false;
        }
        if (dto.getId() == null) {
            return isNewTaskValid(dto, context);
        }
        return isOldTaskValid(dto, context);
    }

    private void buildContext(String message, ConstraintValidatorContext context) {
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }

    private boolean isNewTaskValid(TaskDto dto, ConstraintValidatorContext context) {
        //Inside one project, tasks can't have the same name
        Optional<Task> taskOptional = repository.findByNameAndProjectName(dto.getName(), dto.getProjectName());
        buildContext(message, context);
        return taskOptional.isEmpty();
    }

    private boolean isOldTaskValid(TaskDto dto, ConstraintValidatorContext context) {
        Optional<Task> taskOptional = repository.findById(dto.getId());
        if (taskOptional.isEmpty()) {
            message = MessageUtil.getMessage(messageSource, "task.id.not-found");
            buildContext(message, context);
            return false;
        }
        Task task = taskOptional.get();
        return task.getName().equals(dto.getName()) && task.getProject().getName().equals(dto.getProjectName())
                || isNewTaskValid(dto, context);
    }
}
