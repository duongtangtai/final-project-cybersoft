package com.example.riraproject.task.validation;

import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.project.model.Project;
import com.example.riraproject.task.dto.TaskDto;
import com.example.riraproject.task.model.Task;
import com.example.riraproject.task.repository.TaskRepository;
import com.example.riraproject.task.validation.annotation.UniqueTask;
import com.example.riraproject.task.validation.validator.UniqueTaskValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class UniqueTaskValidatorTest {
    @Mock private TaskRepository repository;
    @Autowired private MessageSource messageSource;
    private UniqueTaskValidator validator;

    @BeforeEach
    void init() {
        validator = new UniqueTaskValidator(repository, messageSource);
    }

    @Test
    void validatorShouldWork() {
        //SETUP
        String message = "some message";
        UniqueTask constraint = Mockito.mock(UniqueTask.class);
        Mockito.when(constraint.message()).thenReturn(message);
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
        //CASE 2: ID IS NULL => NEW TASK => TASK ALREADY EXISTS
        TaskDto dto = TaskDto.builder()
                .name("dto")
                .projectName("project")
                .build();
        Task task = Task.builder()
                .name("dto")
                .project(Project.builder().name("project").build())
                .build();
        Mockito.when(repository.findByNameAndProjectName(dto.getName(), dto.getProjectName()))
                .thenReturn(Optional.of(task));
        Assertions.assertFalse(validator.isValid(dto ,context));
        //CASE 3: ID IS NULL => NEW TASK => TASK DOESN'T EXIST
        Mockito.when(repository.findByNameAndProjectName(dto.getName(), dto.getProjectName()))
                .thenReturn(Optional.empty());
        Assertions.assertTrue(validator.isValid(dto ,context));
        //CASE 4: ID IS NOT NULL => OLD TASK => ID INVALID
        dto.setId(UUID.randomUUID());
        Mockito.when(repository.findById(dto.getId()))
                .thenReturn(Optional.empty());
        String errorMessage = MessageUtil.getMessage(messageSource, "task.id.not-found");
        Mockito.when(context.buildConstraintViolationWithTemplate(errorMessage))
                .thenReturn(builder);
        Assertions.assertFalse(validator.isValid(dto ,context));
        //CASE 5: ID IS NOT NULL => OLD TASK => ID VALID => TASK NAME DOESN'T CHANGE
        Mockito.when(repository.findById(dto.getId()))
                .thenReturn(Optional.of(task));
        Assertions.assertTrue(validator.isValid(dto ,context));
        //CASE 5: ID IS NOT NULL => OLD TASK => ID VALID => TASK NAME CHANGE => THE NEW NAME IS VALID
        dto.setName("newName");
        Mockito.when(repository.findByNameAndProjectName(dto.getName(), dto.getProjectName()))
                .thenReturn(Optional.empty());
        Assertions.assertTrue(validator.isValid(dto ,context));
        //CASE 6: ID IS NOT NULL => OLD TASK => ID VALID => TASK NAME CHANGE => THE NEW NAME IS INVALID
        Mockito.when(repository.findByNameAndProjectName(dto.getName(), dto.getProjectName()))
                .thenReturn(Optional.of(new Task()));
        Assertions.assertFalse(validator.isValid(dto ,context));
    }
}
