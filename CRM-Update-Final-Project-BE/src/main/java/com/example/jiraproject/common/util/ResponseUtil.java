package com.example.jiraproject.common.util;

import com.example.jiraproject.common.dto.ResponseDto;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@UtilityClass
public class ResponseUtil {
    public static ResponseEntity<ResponseDto> error(ValidationException exception, HttpStatus status) {
        return new ResponseEntity<>(
                ResponseDto
                        .builder()
                        .content(null)
                        .hasErrors(true)
                        .errors(ErrorUtil.getErrorMessage(exception))
                        .timeStamp(DateTimeUtil.now())
                        .statusCode(status.value())
                        .build(),
                status
        );
    }

    public static ResponseEntity<ResponseDto> error(MethodArgumentNotValidException exception, HttpStatus status) {
        return new ResponseEntity<>(
                ResponseDto
                        .builder()
                        .content(null)
                        .hasErrors(true)
                        .errors(ErrorUtil.getErrorMessage(exception))
                        .timeStamp(DateTimeUtil.now())
                        .statusCode(status.value())
                        .build(),
                status
        );
    }

    public static ResponseEntity<ResponseDto> error(InvalidFormatException exception, HttpStatus status) {
        return new ResponseEntity<>(
                ResponseDto
                        .builder()
                        .content(null)
                        .hasErrors(true)
                        .errors(ErrorUtil.getErrorMessage(exception))
                        .timeStamp(DateTimeUtil.now())
                        .statusCode(status.value())
                        .build(),
                status
        );
    }

    public static ResponseEntity<ResponseDto> error(ConstraintViolationException exception, HttpStatus status) {
        return new ResponseEntity<>(
                ResponseDto
                        .builder()
                        .content(null)
                        .hasErrors(true)
                        .errors(ErrorUtil.getErrorMessage(exception))
                        .timeStamp(DateTimeUtil.now())
                        .statusCode(status.value())
                        .build(),
                status
        );
    }
    public static ResponseEntity<ResponseDto> get(Object dto, HttpStatus status) {
        return new ResponseEntity<>(
                ResponseDto
                        .builder()
                        .content(dto)
                        .hasErrors(false)
                        .errors(null)
                        .timeStamp(DateTimeUtil.now())
                        .statusCode(status.value())
                        .build(),
                status
        );
    }
}
