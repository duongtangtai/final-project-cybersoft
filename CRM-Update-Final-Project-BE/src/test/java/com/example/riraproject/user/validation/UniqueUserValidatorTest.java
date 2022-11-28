package com.example.riraproject.user.validation;

import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.user.dto.UserDto;
import com.example.riraproject.user.model.User;
import com.example.riraproject.user.repository.UserRepository;
import com.example.riraproject.user.validation.annotation.UniqueUser;
import com.example.riraproject.user.validation.validator.UniqueUserValidator;
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
class UniqueUserValidatorTest {
    @Mock private UserRepository repository;
    @Autowired private MessageSource messageSource;
    private UniqueUserValidator validator;

    @BeforeEach
    void init() {
        validator = new UniqueUserValidator(repository, messageSource);
    }

    @Test
    void validatorShouldWork() {
        //SETUP
        String message = "some message";
        UniqueUser constraint = Mockito.mock(UniqueUser.class);
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
        //CASE 2: DTO IS A OLD USER AND HAS OLD NAME
        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .username("oldUser")
                .build();
        UserDto dto1 = UserDto.builder()
                .id(id)
                .username("oldUser")
                .build();
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(user));
        Assertions.assertTrue(validator.isValid(dto1, context));
        Mockito.verify(context).buildConstraintViolationWithTemplate(captor.capture());
        Assertions.assertEquals(message, captor.getValue());
        //CASE 3: DTO IS A OLD USER AND HAS NEW USERNAME (newUsername is valid)
        UserDto dto2 = UserDto.builder()
                .id(id)
                .username("newUser")
                .build();
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(user));
        Mockito.when(repository.findByUsername("newUser")).thenReturn(Optional.empty());
        Assertions.assertTrue(validator.isValid(dto2, context));
        //CASE 4: DTO IS A OLD USER AND HAS NEW USERNAME (newUsername is invalid)
        Mockito.when(repository.findByUsername("newUser")).thenReturn(Optional.of(new User()));
        Assertions.assertFalse(validator.isValid(dto2, context));
        //CASE 5: DTO IS A NEW USER AND HAS NEW USERNAME (newUsername is valid)
        UserDto dto3 = UserDto.builder() //id is null
                .username("newUser")
                .build();
        Mockito.when(repository.findByUsername("newUser")).thenReturn(Optional.empty());
        Assertions.assertTrue(validator.isValid(dto3, context));
        //CASE 5: DTO IS A NEW USER AND HAS NEW USERNAME (newUsername is invalid)
        Mockito.when(repository.findByUsername("newUser")).thenReturn(Optional.of(new User()));
        Assertions.assertFalse(validator.isValid(dto3, context));
        //CASE 6: DTO IS A OLD USER BUT ID IS INVALID
        UUID invalidId = UUID.randomUUID();
        UserDto dto4 = UserDto.builder()
                .id(invalidId)
                .username("oldUser")
                .build();
        String errorMessage = MessageUtil.getMessage(messageSource,
                "user.id.not-found");
        Mockito.when(context.buildConstraintViolationWithTemplate(errorMessage))
                .thenReturn(builder);
        Mockito.when(repository.findById(invalidId)).thenReturn(Optional.empty());
        Assertions.assertFalse(validator.isValid(dto4, context));
    }
}
