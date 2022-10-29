package com.example.jiraproject.project.validation.validator;

import com.example.jiraproject.common.exception.JiraException;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.project.dto.ProjectDto;
import com.example.jiraproject.project.model.Project;
import com.example.jiraproject.project.repository.ProjectRepository;
import com.example.jiraproject.project.validation.annotation.UniqueProject;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.util.Optional;

@RequiredArgsConstructor
public class UniqueProjectValidator implements ConstraintValidator<UniqueProject, ProjectDto> {
    private final ProjectRepository repository;
    private String message;
    private final MessageSource messageSource;

    @Override
    public void initialize(UniqueProject constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(ProjectDto dto, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        if (dto == null) {
            return false;
        }
        //if ID is null => this is a new project => we check whether the name existed
        if (dto.getId() == null) {
            return isNewProjectValid(dto);
        }
        //if ID is not null => this is an old project => we check whether it's valid
        return isOldProjectValid(dto);
    }

    private boolean isNewProjectValid(ProjectDto dto) {
        Optional<Project> projectOptional = repository.findByName(dto.getName());
        return projectOptional.isEmpty();
    }

    private boolean isOldProjectValid(ProjectDto dto) {
        Project oldProject = repository.findById(dto.getId())
                .orElseThrow(() -> new ValidationException(
                        MessageUtil.getMessage(messageSource, "project.id.not-found")));
        //if oldName doesn't equal to newName => check whether the newName is valid
        return oldProject.getName().equals(dto.getName()) || isNewProjectValid(dto);
    }
}
