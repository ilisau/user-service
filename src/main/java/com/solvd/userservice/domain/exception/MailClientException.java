package com.solvd.userservice.domain.exception;

import lombok.Getter;

@Getter
public class MailClientException extends RuntimeException {

    public MailClientException(String message) {
        super(message);
    }

}
