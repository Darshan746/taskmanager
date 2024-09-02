package com.encora.taskmanager.enumconstants;

public enum TaskStatus {
    NEW("New Task Created"),
    IN_PROGRESS("Task is in Progress"),
    CODE_REVIEW("Task is under Code Review"),
    IN_TESTING("Task is under Testing"),
    DONE("Task is Completed");

    private final String description;
    private TaskStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
