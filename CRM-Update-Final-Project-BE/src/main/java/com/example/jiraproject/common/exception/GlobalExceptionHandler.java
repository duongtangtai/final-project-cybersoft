package com.example.jiraproject.common.exception;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.ResponseUtil;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private void logError(Exception e) {
        log.error("{} occurred.", e.getClass());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseDto> handleValidationException(ValidationException exception) {
        logError(exception);
        return ResponseUtil.error(exception, HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception may throw from FIELD constraints
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        logError(exception);
        return ResponseUtil.error(exception, HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception may throw when trying to map the request body into the DTO
     */
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ResponseDto> handleInvalidFormatException(InvalidFormatException exception) {
        logError(exception);
        return ResponseUtil.error(exception, HttpStatus.BAD_REQUEST);
    }

    /**
     * ConstraintViolationException implements ValidationException. It catches the exception before
     * ValidationException.
     * Exception may throw from PARAMETER constraints
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDto> handleConstraintViolationException(ConstraintViolationException exception) {
        logError(exception);
        return ResponseUtil.error(exception, HttpStatus.BAD_REQUEST);
    }
}
