package com.example.riraproject.project.validation;

import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.project.dto.ProjectDto;
import com.example.riraproject.project.model.Project;
import com.example.riraproject.project.repository.ProjectRepository;
import com.example.riraproject.project.validation.annotation.UniqueProject;
import com.example.riraproject.project.validation.validator.UniqueProjectValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UniqueProjectValidatorTest {
    @Mock private ProjectRepository repository;
    @Autowired private MessageSource messageSource;
    private UniqueProjectValidator validator;

    @BeforeEach
    void init() {
        validator = new UniqueProjectValidator(repository, messageSource);
    }

    @Test
    void validatorShouldWork() {
        //SETUP
        String message = "some message";
        UniqueProject constraint = Mockito.mock(UniqueProject.class);
        Mockito.when(constraint.message()).thenReturn(message);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder =
                Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext newContext = Mockito.mock(ConstraintValidatorContext.class);
        Mockito.when(context.buildConstraintViolationWithTemplate(message))
                .thenReturn(builder);
        Mockito.when(builder.addConstraintViolation())
                .thenReturn(newContext);
        Assertions.assertDoesNotThrow(() -> validator.initialize(constraint));
        //TRY
        //CASE 1: DTO IS NULL
        Assertions.assertFalse(validator.isValid(null ,context));
        //CASE 2: DTO IS A OLD PROJECT AND HAS OLD NAME
        UUID id = UUID.randomUUID();
        Project project = Project.builder()
                .id(id)
                .name("oldProject")
                .build();
        ProjectDto dto1 = ProjectDto.builder()
                .id(id)
                .name("oldProject")
                .build();
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(project));
        Assertions.assertTrue(validator.isValid(dto1, context));
        Mockito.verify(context).buildConstraintViolationWithTemplate(captor.capture());
        Assertions.assertEquals(message, captor.getValue());
        //CASE 3: DTO IS A OLD PROJECT AND HAS NEW NAME (newName is valid)
        ProjectDto dto2 = ProjectDto.builder()
                .id(id)
                .name("newProject")
                .build();
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(project));
        Mockito.when(repository.findByName("newProject")).thenReturn(Optional.empty());
        Assertions.assertTrue(validator.isValid(dto2, context));
        //CASE 4: DTO IS A OLD PROJECT AND HAS NEW NAME (newName is invalid)
        Mockito.when(repository.findByName("newProject")).thenReturn(Optional.of(new Project()));
        Assertions.assertFalse(validator.isValid(dto2, context));
        //CASE 5: DTO IS A NEW PROJECT AND HAS NEW NAME (newName is valid)
        ProjectDto dto3 = ProjectDto.builder() //id is null
                .name("newProject")
                .build();
        Mockito.when(repository.findByName("newProject")).thenReturn(Optional.empty());
        Assertions.assertTrue(validator.isValid(dto3, context));
        //CASE 5: DTO IS A NEW PROJECT AND HAS NEW NAME (newName is invalid)
        Mockito.when(repository.findByName("newProject")).thenReturn(Optional.of(new Project()));
        Assertions.assertFalse(validator.isValid(dto3, context));
        //CASE 6: DTO IS A OLD PROJECT BUT ID IS INVALID
        UUID invalidId = UUID.randomUUID();
        ProjectDto dto4 = ProjectDto.builder()
                .id(invalidId)
                .name("oldProject")
                .build();
        String errorMessage = MessageUtil.getMessage(messageSource,
                "project.id.not-found");
        Mockito.when(context.buildConstraintViolationWithTemplate(errorMessage))
                .thenReturn(builder);
        Mockito.when(repository.findById(invalidId)).thenReturn(Optional.empty());
        Assertions.assertFalse(validator.isValid(dto4, context));
    }
}
