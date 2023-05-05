package com.solvd.userservice.domain.exception;

public class UserNotFoundException extends RuntimeException {

    /**
     * Constructor of UserNotFoundException.
     *
     * @param message message
     */
    public UserNotFoundException(final String message) {
        super(message);
    }

}
