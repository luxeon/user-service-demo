package com.example.user.exception;

import com.example.user.rest.dto.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .sorted(comparing(FieldError::getField)
                        .thenComparing(DefaultMessageSourceResolvable::getDefaultMessage))
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(joining(", "));
        return new ErrorResponse(errorMessage);
    }
} 