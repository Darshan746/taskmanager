package com.encora.taskmanager.dto.request;

import com.encora.taskmanager.annotations.FutureOrPresentDate;
import com.encora.taskmanager.constant.ValidationConstants;
import com.encora.taskmanager.enumconstants.TaskStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class TaskCreateRequest {

    @NotNull(message = ValidationConstants.TASK_NAME_SHOULD_NOT_BE_NULL)
    @NotEmpty(message = ValidationConstants.TASK_NAME_SHOULD_NOT_BE_EMPTY)
    @Size(min = 5, max = 500, message = ValidationConstants.TASK_TITLE_MUST_BE_BETWEEN_5_AND_500_CHARACTERS)
    private String taskTitle;

    @NotNull(message = ValidationConstants.TASK_DESCRIPTION_SHOULD_NOT_BE_NULL)
    @NotEmpty(message = ValidationConstants.TASK_DESCRIPTION_SHOULD_NOT_BE_EMPTY)
    @Size(min = 10, max = 1000, message = ValidationConstants.TASK_DESCRIPTION_MUST_BE_BETWEEN_10_AND_1000_CHARACTERS)
    private String taskDescription;


    @FutureOrPresentDate
    @NotNull(message = ValidationConstants.TASK_DUE_DATE_SHOULD_NOT_BE_NULL)
    private LocalDate taskDueDate;

    private TaskStatus taskStatus = TaskStatus.NEW;

    @NotNull(message = "userId Should not be null")
    private long userId;
}
