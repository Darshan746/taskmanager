package com.encora.taskmanager.exceptionHandlers;

import lombok.Data;

@Data
public class UserAlreadyExistException extends RuntimeException {

    private String message;

    public UserAlreadyExistException(String message) {
        super(message);
        this.message = message;
    }
}
