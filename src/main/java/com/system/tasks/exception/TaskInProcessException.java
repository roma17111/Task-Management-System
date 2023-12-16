package com.system.tasks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class TaskInProcessException extends Exception {

    public TaskInProcessException(String message) {
        super(message);
    }
}
