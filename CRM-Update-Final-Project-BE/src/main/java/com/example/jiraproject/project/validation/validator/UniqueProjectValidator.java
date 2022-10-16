package com.example.jiraproject.project.validation.validator;

import com.example.jiraproject.project.dto.ProjectDto;
import com.example.jiraproject.project.model.Project;
import com.example.jiraproject.project.repository.ProjectRepository;
import com.example.jiraproject.project.validation.annotation.UniqueProject;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
public class UniqueProjectValidator implements ConstraintValidator<UniqueProject, ProjectDto> {
    private final ProjectRepository repository;
    private String message;

    @Override
    public void initialize(UniqueProject constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(ProjectDto projectDto, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        Optional<Project> projectOptional = repository.findByName(projectDto.getName());
        return projectOptional.isEmpty();
    }
}
