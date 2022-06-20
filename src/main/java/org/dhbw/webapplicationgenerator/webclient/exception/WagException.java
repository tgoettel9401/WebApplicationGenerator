package org.dhbw.webapplicationgenerator.webclient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class WagException extends RuntimeException {

    public WagException(String exceptionText) {
        super(exceptionText);
    }

}
