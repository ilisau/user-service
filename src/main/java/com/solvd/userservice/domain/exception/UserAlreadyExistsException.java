package com.solvd.userservice.domain.exception;

public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructor of UserAlreadyExistsException.
     *
     * @param message message
     */
    public UserAlreadyExistsException(final String message) {
        super(message);
    }

}
