package com.system.tasks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidMailException extends Exception{

    public InvalidMailException(String message) {
        super(message);
    }
}
