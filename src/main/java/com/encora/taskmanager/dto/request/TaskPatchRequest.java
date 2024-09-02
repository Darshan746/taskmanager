package com.encora.taskmanager.dto.request;

import com.encora.taskmanager.enumconstants.TaskStatus;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class TaskPatchRequest {
    private String taskTitle;
    private String taskDescription;
    private LocalDate taskDueDate;
    private TaskStatus taskStatus;
    private Long userId;
}