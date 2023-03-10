package com.solvd.userservice.web.controller;

import com.solvd.userservice.domain.exception.ExceptionMessage;
import com.solvd.userservice.domain.exception.PasswordMismatchException;
import com.solvd.userservice.domain.exception.UserAlreadyExistsException;
import com.solvd.userservice.domain.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage handleUserNotFound(UserNotFoundException e) {
        return new ExceptionMessage(e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleUserAlreadyExists(UserAlreadyExistsException e) {
        return new ExceptionMessage(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleIllegalState(IllegalStateException e) {
        return new ExceptionMessage(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleIllegalArgument(IllegalArgumentException e) {
        return new ExceptionMessage(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ExceptionMessage exceptionBody = new ExceptionMessage("Validation failed");
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        exceptionBody.setDetails(errors.stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
        return exceptionBody;
    }

    @ExceptionHandler(PasswordMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handlePasswordMismatchException(PasswordMismatchException e) {
        return new ExceptionMessage(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleConstraintViolationException(ConstraintViolationException e) {
        ExceptionMessage exceptionBody = new ExceptionMessage("Validation failed");
        exceptionBody.setDetails(e.getConstraintViolations().stream()
                .collect(Collectors.toMap(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)));
        return exceptionBody;
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleBindException(BindException e) {
        ExceptionMessage exceptionBody = new ExceptionMessage("Validation failed");
        Map<String, String> details = new HashMap<>();
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        errors.forEach(error -> details.put(error.getField(), error.getDefaultMessage()));
        exceptionBody.setDetails(details);
        return exceptionBody;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage handleException(Exception e) {
        return new ExceptionMessage("Internal error");
    }

}
