package com.solvd.userservice.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionMessage {

    private String message;
    private Map<String, String> details;

    /**
     * Constructor of ExceptionMessage.
     *
     * @param message message
     */
    public ExceptionMessage(final String message) {
        this.message = message;
    }

}
