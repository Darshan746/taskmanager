package com.encora.taskmanager.service;

import com.encora.taskmanager.dao.TaskDao;
import com.encora.taskmanager.dao.UserDao;
import com.encora.taskmanager.dto.request.TaskPatchRequest;
import com.encora.taskmanager.entity.Task;
import com.encora.taskmanager.entity.User;
import com.encora.taskmanager.enumconstants.TaskStatus;
import com.encora.taskmanager.exceptionHandlers.TaskDeleteException;
import com.encora.taskmanager.exceptionHandlers.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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

    @Override
    public Page<Task> findAllTaskByUserId(long userId, int page, int size) {
        return taskDao.findByUserId(userId, PageRequest.of(page, size));
    }

    @Override
    public List<Task> getTaskByDueDateAndUser(LocalDate localDate, User user) {
        return taskDao.findByTaskDueDateAndusr(localDate,user);
    }

    @Override
    public Task getTaskByTaskId(long taskId) {
        Optional<Task> optionalTask = taskDao.findById(taskId);
        if (optionalTask.isPresent()){
            return optionalTask.get();
        }else {
            throw new TaskNotFoundException("task with id "+taskId+" "+"doesn't exist");
        }
    }

    @Override
    public Page<Task> findAllTaskByUserId(long userId, int page, int size, TaskStatus taskStatus, String sortBy) {
        if (taskStatus != null) {
            return taskDao.findByUserIdAndTaskStatus(userId, taskStatus, PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortBy), "taskDueDate")));
        } else {
            return taskDao.findByUserId(userId, PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortBy), "taskDueDate")));
        }
    }
}
