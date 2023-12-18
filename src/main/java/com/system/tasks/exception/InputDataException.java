package com.system.tasks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InputDataException extends Exception{

    public InputDataException() {
    }

    public InputDataException(String message) {
        super(message);
    }
}
