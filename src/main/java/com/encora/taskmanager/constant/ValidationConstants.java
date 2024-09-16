package com.encora.taskmanager.constant;

public class ValidationConstants {

    //User related message here
    public static final String NAME_IS_REQUIRED = "Name is required";
    public static final String EMAIL_IS_REQUIRED = "Email is required";
    public static final String PASSWORD_IS_REQUIRED = "Password is required";
    public static final String NAME_SHOULD_NOT_BE_EMPTY = "Name should not be empty";
    public static final String EMAIL_SHOULD_NOT_BE_EMPTY = "Email should not be empty";
    public static final String PASSWORD_SHOULD_NOT_BE_EMPTY = "Password should not be empty";
    public static final String NAME_MUST_BE_BETWEEN_2_AND_50_CHARACTERS = "Name must be between 2 and 50 characters";
    public static final String EMAIL_MUST_BE_VALID = "Email must be valid";
    public static final String ROLE_SHOULD_NOT_BE_EMPTY = "Role should not be empty";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final String PASSWORD_CRITERIA = "Password must meet the following criteria: \n" +
            "At least 8 characters long\n" +
            "At least one uppercase letter\n" +
            "At least one lowercase letter\n" +
            "At least one digit\n" +
            "At least one special character\n" +
            "No whitespace allowed";

    //Task related message here
    public static final String TASK_NAME_SHOULD_NOT_BE_EMPTY = "Task name should not be empty";
    public static final String TASK_NAME_SHOULD_NOT_BE_NULL = "Task name should not be empty";
    public static final String TASK_DESCRIPTION_SHOULD_NOT_BE_EMPTY = "Task description should not be empty";
    public static final String TASK_DESCRIPTION_SHOULD_NOT_BE_NULL = "Task description should not be empty";
    public static final String TASK_DUE_DATE_SHOULD_NOT_BE_NULL="Task due date should not be null";
    public static final String TASK_TITLE_MUST_BE_BETWEEN_5_AND_500_CHARACTERS = "Task title must be between 5 to 500 character";
    public static final String TASK_DESCRIPTION_MUST_BE_BETWEEN_10_AND_1000_CHARACTERS = "Task description must be between 10 to 1000 character";





}
