package org.dhbw.webapplicationgenerator.webclient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends Exception {
    public ValidationException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
