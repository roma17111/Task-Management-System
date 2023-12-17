package com.system.tasks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TaskDeleteException extends RuntimeException{

    public TaskDeleteException(String message) {
        super(message);
    }
}
