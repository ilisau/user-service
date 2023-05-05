package com.solvd.userservice.domain.exception;

public class InvalidTokenException extends RuntimeException {

    /**
     * Constructor of InvalidTokenException.
     *
     * @param message message
     */
    public InvalidTokenException(final String message) {
        super(message);
    }

}
