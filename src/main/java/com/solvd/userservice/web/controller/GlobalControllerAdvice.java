package com.solvd.userservice.web.controller;

import com.solvd.userservice.domain.exception.ExceptionMessage;
import com.solvd.userservice.domain.exception.InvalidTokenException;
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

    /**
     * Handles UserNotFoundException.
     *
     * @param e UserNotFoundException
     * @return ExceptionMessage
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage handleUserNotFound(final UserNotFoundException e) {
        return new ExceptionMessage(e.getMessage());
    }

    /**
     * Handles UserAlreadyExistsException.
     *
     * @param e UserAlreadyExistsException
     * @return ExceptionMessage
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleUserAlreadyExists(
            final UserAlreadyExistsException e
    ) {
        return new ExceptionMessage(e.getMessage());
    }

    /**
     * Handles IllegalStateException.
     *
     * @param e IllegalStateException
     * @return ExceptionMessage
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleIllegalState(final IllegalStateException e) {
        return new ExceptionMessage(e.getMessage());
    }

    /**
     * Handles IllegalArgumentException.
     *
     * @param e IllegalArgumentException
     * @return ExceptionMessage
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleIllegalArgument(
            final IllegalArgumentException e
    ) {
        return new ExceptionMessage(e.getMessage());
    }

    /**
     * Handles MethodArgumentNotValidException.
     *
     * @param e MethodArgumentNotValidException
     * @return ExceptionMessage
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e
    ) {
        ExceptionMessage exceptionBody =
                new ExceptionMessage("Validation failed");
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        exceptionBody.setDetails(errors.stream()
                .collect(Collectors.toMap(FieldError::getField,
                        FieldError::getDefaultMessage)));
        return exceptionBody;
    }

    /**
     * Handles PasswordMismatchException.
     *
     * @param e PasswordMismatchException
     * @return ExceptionMessage
     */
    @ExceptionHandler(PasswordMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handlePasswordMismatchException(
            final PasswordMismatchException e
    ) {
        return new ExceptionMessage(e.getMessage());
    }

    /**
     * Handles ConstraintViolationException.
     *
     * @param e ConstraintViolationException
     * @return ExceptionMessage
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleConstraintViolationException(
            final ConstraintViolationException e
    ) {
        ExceptionMessage exceptionBody =
                new ExceptionMessage("Validation failed");
        exceptionBody.setDetails(e.getConstraintViolations().stream()
                .collect(Collectors.toMap(v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage)));
        return exceptionBody;
    }

    /**
     * Handles BindException.
     *
     * @param e BindException
     * @return ExceptionMessage
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleBindException(final BindException e) {
        ExceptionMessage exceptionBody =
                new ExceptionMessage("Validation failed");
        Map<String, String> details = new HashMap<>();
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        errors.forEach(error -> details.put(error.getField(),
                error.getDefaultMessage()));
        exceptionBody.setDetails(details);
        return exceptionBody;
    }

    /**
     * Handles InvalidTokenException.
     *
     * @param e InvalidTokenException
     * @return ExceptionMessage
     */
    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleInvalidTokenException(
            final InvalidTokenException e
    ) {
        return new ExceptionMessage(e.getMessage());
    }

    /**
     * Handles Exception.
     *
     * @param e Exception
     * @return ExceptionMessage
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage handleException(final Exception e) {
        e.printStackTrace();
        return new ExceptionMessage("Internal error");
    }

}
