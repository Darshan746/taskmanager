package com.encora.taskmanager.service;

import com.encora.taskmanager.dao.TaskDao;
import com.encora.taskmanager.dao.UserDao;
import com.encora.taskmanager.dto.request.TaskPatchRequest;
import com.encora.taskmanager.entity.Task;
import com.encora.taskmanager.entity.User;
import com.encora.taskmanager.exceptionHandlers.TaskDeleteException;
import com.encora.taskmanager.exceptionHandlers.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private UserDao userDao;

    @Override
    public long createTask(final Task task) {
        final Optional<User> user = userDao.findById(task.getUser().getId());
        if (user.isPresent()) {
            task.setUser(user.get());
            return taskDao.save(task).getId();
        } else {
            throw new UsernameNotFoundException("userId" + task.getUser().getId() + "doesn't exist");
        }
    }

    @Override
    public boolean deleteTask(final long taskId) {
        Optional<Task> task = taskDao.findById(taskId);
        if (task.isPresent()) {
            try {
                taskDao.delete(task.get());
            } catch (Exception exception) {
                throw new TaskDeleteException(exception.getMessage());
            }
            return true;
        } else {
            throw new TaskNotFoundException("Task with Id " + taskId + " not found");
        }
    }

    @Override
    public Task patchUpdateTask(final long taskId, final TaskPatchRequest request) {
        final Optional<Task> existingTask = taskDao.findById(taskId);
        if (existingTask.isPresent()) {
            final Task taskToUpdate = existingTask.get();
            if (request.getTaskTitle() != null) {
                taskToUpdate.setTaskTitle(request.getTaskTitle());
            }
            if (request.getTaskDescription() != null) {
                taskToUpdate.setTaskDescription(request.getTaskDescription());
            }
            if (request.getTaskDueDate() != null) {
                taskToUpdate.setTaskDueDate(request.getTaskDueDate());
            }
            if (request.getTaskStatus() != null) {
                taskToUpdate.setTaskStatus(request.getTaskStatus());
            }
            if (request.getUserId() != null) {
                final Optional<User> user = userDao.findById(request.getUserId());
                if (user.isPresent()) {
                    taskToUpdate.setUser(user.get());
                }else {
                    throw new UsernameNotFoundException("userId" + request.getUserId() + "doesn't exist");
                }
            }
            return taskDao.save(taskToUpdate);
        } else {
            throw new TaskNotFoundException("Task with ID " + taskId + " not found");
        }
    }


}
