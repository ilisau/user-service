package com.solvd.userservice.domain.exception;

public class PasswordMismatchException extends RuntimeException {

    /**
     * Constructor of PasswordMismatchException.
     *
     * @param message message
     */
    public PasswordMismatchException(final String message) {
        super(message);
    }

}
