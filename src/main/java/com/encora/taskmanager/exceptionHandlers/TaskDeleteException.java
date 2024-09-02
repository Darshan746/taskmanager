package com.encora.taskmanager.exceptionHandlers;

public class TaskDeleteException extends RuntimeException{
    public TaskDeleteException(String message) {
        super(message);
    }
}
