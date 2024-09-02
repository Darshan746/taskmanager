package com.encora.taskmanager.converter;

import com.encora.taskmanager.dao.UserDao;
import com.encora.taskmanager.dto.request.TaskCreateRequest;
import com.encora.taskmanager.dto.request.UserSignUpRequest;
import com.encora.taskmanager.entity.Task;
import com.encora.taskmanager.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestToEntityConverter {

    @Autowired
    UserDao userDao;

    public User convertUserRequestToUserEntity(final UserSignUpRequest userSignUpRequest) {
        final User user = new User();
        user.setUsername(userSignUpRequest.getName());
        user.setEmail(userSignUpRequest.getEmail());
        user.setPassword(userSignUpRequest.getPassword());
        user.setRoles(userSignUpRequest.getRoles());
        return user;
    }

    public Task convertTaskCreateRequestToTaskEntity(final TaskCreateRequest request, final User user) {
        final Task task = new Task();
        task.setTaskTitle(request.getTaskTitle());
        task.setTaskDescription(request.getTaskDescription());
        task.setTaskDueDate(request.getTaskDueDate());
        task.setTaskStatus(request.getTaskStatus());
        task.setUser(user);
        return task;
    }


}
