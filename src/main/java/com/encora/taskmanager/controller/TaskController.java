package com.encora.taskmanager.controller;

import com.encora.taskmanager.converter.RequestToEntityConverter;
import com.encora.taskmanager.dto.request.TaskCreateRequest;
import com.encora.taskmanager.dto.request.TaskPatchRequest;
import com.encora.taskmanager.entity.Task;
import com.encora.taskmanager.entity.User;
import com.encora.taskmanager.enumconstants.TaskStatus;
import com.encora.taskmanager.jwt.JwtService;
import com.encora.taskmanager.response.ApiResponse;
import com.encora.taskmanager.service.TaskService;
import com.encora.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {

    @Autowired
    private RequestToEntityConverter requestToEntityConverter;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private JwtService jwtService;

    @PostMapping(value = "/api/tasks")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<String>> createNewTask(final @Valid @RequestBody TaskCreateRequest request) {
        final Optional<User> user = userService.findById(request.getUserId());
        if (user.isPresent()) {
            final Task task = requestToEntityConverter.convertTaskCreateRequestToTaskEntity(request, userService.findById(request.getUserId()).get());
            long taskId = taskService.createTask(task);
            return ResponseEntity.ok().body(new ApiResponse<>("Task Created Successfully with Id " + taskId, null));
        } else {
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


    @GetMapping("/tasks11")
    public ResponseEntity<ApiResponse<Page<Task>>> getAllTaskByUserId(@RequestHeader("Authorization") String authorizationHeader,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        final String token = authorizationHeader.substring(7);
        final String userName = jwtService.extractUserName(token);
        final User user = userService.findByUsername(userName).get();
        Page<Task> pages = taskService.findAllTaskByUserId(user.getId(), 0, 10);
        return ResponseEntity.ok().body(new ApiResponse<>(pages, null));
    }


    @GetMapping("/tasks/{id}")
    public ResponseEntity<ApiResponse<Task>> getTaskByTaskId(@PathVariable Long id) {
        Task taskByTaskId = taskService.getTaskByTaskId(id);
        return ResponseEntity.ok().body(new ApiResponse<>(taskByTaskId, null));
    }

    @PostMapping("/tasks")
    public ResponseEntity<ApiResponse<List<Task>>> getTaskByDateAndUser(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("dueDate") LocalDate localDate) {
        final String token = authorizationHeader.substring(7);
        final String userName = jwtService.extractUserName(token);
        final User user = userService.findByUsername(userName).get();
        List<Task> tasks = taskService.getTaskByDueDateAndUser(localDate, user);
        return ResponseEntity.ok().body(new ApiResponse<>(tasks, null));
    }

    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<Page<Task>>> getAllTaskByUserId(@RequestHeader("Authorization") String authorizationHeader,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size,
                                                                      @RequestParam(required = false) TaskStatus taskStatus,
                                                                      @RequestParam(defaultValue = "ASC", required = false) String sortBy) {
        final String token = authorizationHeader.substring(7);
        final String userName = jwtService.extractUserName(token);
        final User user = userService.findByUsername(userName).get();
        Page<Task> pages = taskService.findAllTaskByUserId(user.getId(), page, size, taskStatus, sortBy);
        return ResponseEntity.ok().body(new ApiResponse<>(pages, null));
    }

}
