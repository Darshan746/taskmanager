package com.encora.taskmanager.service;

import com.encora.taskmanager.dto.request.TaskPatchRequest;
import com.encora.taskmanager.entity.Task;

public interface TaskService {
    long createTask(Task task);
    boolean deleteTask(long taskId);
    Task patchUpdateTask(long taskId, TaskPatchRequest request);

}
