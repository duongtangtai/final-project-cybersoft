package com.example.riraproject.common.exception;

import com.example.riraproject.common.dto.ResponseDto;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerTest {

    private GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private final String message = "some error message";

    private void checkResult(ResponseEntity<ResponseDto> result) {
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto response = result.getBody();
        Assertions.assertNull(response.getContent());
        Assertions.assertTrue(response.isHasErrors());
        Assertions.assertEquals(message, response.getErrors().get(0));
        Assertions.assertNotNull(response.getTimeStamp());
    }

    @Test
    void validationExceptionTest() {
        //SETUP
        ValidationException exception = Mockito.mock(ValidationException.class);
        Mockito.when(exception.getMessage()).thenReturn(message);
        //TRY
        ResponseEntity<ResponseDto> result = handler.handleValidationException(exception);
        //CHECK RESULT
        checkResult(result);
    }

    @Test
    void methodArgumentNotValidExceptionTest() {
        //SETUP
        MethodArgumentNotValidException exception = Mockito.mock(MethodArgumentNotValidException.class);
        ObjectError error = Mockito.mock(ObjectError.class);
        Mockito.when(exception.getAllErrors()).thenReturn(List.of(error));
        Mockito.when(error.getDefaultMessage()).thenReturn(message);
        //TRY
        ResponseEntity<ResponseDto> result = handler.handleMethodArgumentNotValidException(exception);
        //CHECK RESULT
        checkResult(result);
    }

    @Test
    void invalidFormatExceptionTest() {
        //SETUP
        InvalidFormatException exception = Mockito.mock(InvalidFormatException.class);
        Mockito.when(exception.getTargetType()).thenReturn(null);
        Mockito.when(exception.getOriginalMessage()).thenReturn(message);
        //TRY
        ResponseEntity<ResponseDto> result = handler.handleInvalidFormatException(exception);
        //CHECK RESULT
        checkResult(result);
    }

    @Test
    void constraintViolationExceptionTest() {
        //SETUP
        ConstraintViolationException exception = Mockito.mock(ConstraintViolationException.class);
        ConstraintViolation constraint = Mockito.mock(ConstraintViolation.class);
        Mockito.when(exception.getConstraintViolations()).thenReturn(Set.of(constraint));
        Mockito.when(constraint.getMessage()).thenReturn(message);
        //TRY
        ResponseEntity<ResponseDto> result = handler.handleConstraintViolationException(exception);
        //CHECK RESULT
        checkResult(result);
    }

    @Test
    void riraAuthenticationExceptionTest() {
        //SETUP
        RiraAuthenticationException exception = Mockito.mock(RiraAuthenticationException.class);
        Mockito.when(exception.getMessage()).thenReturn(message);
        //TRY
        ResponseEntity<ResponseDto> result = handler.handleRiraAuthenticationException(exception);
        //CHECK RESULT
        checkResult(result);
    }

    @Test
    void riraAuthorizationException() {
        //SETUP
        RiraAuthorizationException exception = Mockito.mock(RiraAuthorizationException.class);
        Mockito.when(exception.getMessage()).thenReturn(message);
        //TRY
        ResponseEntity<ResponseDto> result = handler.handleRiraAuthorizationException(exception);
        //CHECK RESULT
        checkResult(result);
    }

    @Test
    void riraFileUploadExceptionTest() {
        RiraFileUploadException exception = Mockito.mock(RiraFileUploadException.class);
        Mockito.when(exception.getMessage()).thenReturn(message);
        //TRY
        ResponseEntity<ResponseDto> result = handler.handleRiraFileUploadException(exception);
        //CHECK RESULT
        checkResult(result);
    }
}
