package com.example.riraproject.common.exception;

import com.example.riraproject.common.dto.ResponseDto;
import com.example.riraproject.common.util.ResponseUtil;
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
     * Exception may throw from FIELD constraints such as (size, not-blank, our customized-constraints...)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        logError(exception);
        return ResponseUtil.error(exception, HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception may throw when trying to map the request body into the DTO
     * example:
     *      - Mapping an invalid UUID format into the "ID" field
     *      - Mapping an invalid Date format into the "Date" field
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
    /**
     * Exception from Authentication
     */
    @ExceptionHandler(RiraAuthenticationException.class)
    public ResponseEntity<ResponseDto> handleRiraAuthenticationException(RiraAuthenticationException exception) {
        logError(exception);
        return ResponseUtil.error(exception, HttpStatus.BAD_REQUEST);
    }
    /**
     * Exception from Authorization
     */
    @ExceptionHandler(RiraAuthorizationException.class)
    public ResponseEntity<ResponseDto> handleRiraAuthorizationException(RiraAuthorizationException exception) {
        logError(exception);
        return ResponseUtil.error(exception, HttpStatus.FORBIDDEN);
    }
    /**
     * Exception from FileUpload
     */
    @ExceptionHandler(RiraFileUploadException.class)
    public ResponseEntity<ResponseDto> handleRiraFileUploadException(RiraFileUploadException exception) {
        logError(exception);
        return ResponseUtil.error(exception, HttpStatus.BAD_REQUEST);
    }
}
