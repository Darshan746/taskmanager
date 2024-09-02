package com.encora.taskmanager.controller;

import com.encora.taskmanager.converter.RequestToEntityConverter;
import com.encora.taskmanager.dto.request.TaskCreateRequest;
import com.encora.taskmanager.dto.request.TaskPatchRequest;
import com.encora.taskmanager.entity.Task;
import com.encora.taskmanager.entity.User;
import com.encora.taskmanager.response.ApiResponse;
import com.encora.taskmanager.service.TaskService;
import com.encora.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class TaskController {

    @Autowired
    private RequestToEntityConverter requestToEntityConverter;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @PostMapping(value = "/tasks")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<String>> createNewTask(final @Valid @RequestBody TaskCreateRequest request) {
       final Optional<User> user =  userService.findById(request.getUserId());
       if (user.isPresent()){
           final Task task = requestToEntityConverter.convertTaskCreateRequestToTaskEntity(request, userService.findById(request.getUserId()).get());
           long taskId = taskService.createTask(task);
           return ResponseEntity.ok().body(new ApiResponse<>("Task Created Successfully with Id "+taskId, null));
       }else {
           throw new UsernameNotFoundException("please provide the valid UserId");
       }

    }

    @DeleteMapping(value = "/tasks/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTask(@PathVariable("id") long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().body(new ApiResponse<>("Task " + id + " Deleted Successfully", null));
    }

    @PatchMapping("/tasks/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<String>> patchUpdateTask(
            @PathVariable("id") long id,
            @Valid @RequestBody TaskPatchRequest request) {
        Task updatedTask = taskService.patchUpdateTask(id, request);
        return ResponseEntity.ok().body(new ApiResponse<>(
                "Task " + updatedTask.getId() + " updated successfully", null));

    }
}
