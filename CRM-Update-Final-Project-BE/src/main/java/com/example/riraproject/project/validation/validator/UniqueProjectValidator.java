package com.example.riraproject.project.validation.validator;

import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.project.dto.ProjectDto;
import com.example.riraproject.project.model.Project;
import com.example.riraproject.project.repository.ProjectRepository;
import com.example.riraproject.project.validation.annotation.UniqueProject;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
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
    public boolean isValid(ProjectDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            buildContext(message, context);
            return false;
        }
        //if ID is null => this is a new project => we check whether the name existed
        if (dto.getId() == null) {
            return isNewProjectValid(dto, context);
        }
        //if ID is not null => this is an old project => we check whether it's valid
        return isOldProjectValid(dto, context);
    }

    private void buildContext(String message, ConstraintValidatorContext context) {
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }

    private boolean isNewProjectValid(ProjectDto dto, ConstraintValidatorContext context) {
        Optional<Project> projectOptional = repository.findByName(dto.getName());
        buildContext(message, context);
        return projectOptional.isEmpty();
    }

    private boolean isOldProjectValid(ProjectDto dto, ConstraintValidatorContext context) {
        Optional<Project> projectOptional = repository.findById(dto.getId());
        if (projectOptional.isEmpty()) {
            buildContext(message, context);
            return false;
        }
        //if oldName doesn't equal to newName => check whether the newName is valid
        Project project = projectOptional.get();
        return project.getName().equals(dto.getName()) || isNewProjectValid(dto, context);
    }
}
