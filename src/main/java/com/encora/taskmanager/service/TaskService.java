package com.encora.taskmanager.service;

import com.encora.taskmanager.dto.request.TaskPatchRequest;
import com.encora.taskmanager.entity.Task;
import com.encora.taskmanager.entity.User;
import com.encora.taskmanager.enumconstants.TaskStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;


public interface TaskService {
    long createTask(Task task);
    boolean deleteTask(long taskId);
    Task patchUpdateTask(long taskId, TaskPatchRequest request);
    Page<Task> findAllTaskByUserId(long userId, int page, int size);
    Task getTaskByTaskId(long taskId);
    List<Task> getTaskByDueDateAndUser(LocalDate localDate, User user);
    Page<Task> findAllTaskByUserId(long userId, int page, int size, TaskStatus taskStatus, String sortBy);


}
