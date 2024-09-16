package com.encora.taskmanager.controller;

import com.encora.taskmanager.converter.RequestToEntityConverter;
import com.encora.taskmanager.dto.request.TaskCreateRequest;
import com.encora.taskmanager.dto.request.TaskPatchRequest;
import com.encora.taskmanager.entity.Task;
import com.encora.taskmanager.entity.User;
import com.encora.taskmanager.jwt.JwtService;
import com.encora.taskmanager.response.ApiResponse;
import com.encora.taskmanager.service.TaskService;
import com.encora.taskmanager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private RequestToEntityConverter requestToEntityConverter;

    @Mock
    private UserService userService;

    @Mock
    private TaskService taskService;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNewTask_Success() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setUserId(1L);

        User user = new User();
        user.setId(1L);

        Task task = new Task();
        task.setId(1L);

        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(requestToEntityConverter.convertTaskCreateRequestToTaskEntity(any(), any())).thenReturn(task);
        when(taskService.createTask(any())).thenReturn(1L);

        ResponseEntity<ApiResponse<String>> response = taskController.createNewTask(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Task Created Successfully with Id 1", response.getBody().getData());
    }

    @Test
    void testCreateNewTask_UserNotFound() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setUserId(1L);

        when(userService.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> taskController.createNewTask(request));
    }

    @Test
    void testDeleteTask_Success() {
        when(taskService.deleteTask(1L)).thenReturn(true);

        ResponseEntity<ApiResponse<String>> response = taskController.deleteTask(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Task 1 Deleted Successfully", response.getBody().getData());
    }

    @Test
    void testPatchUpdateTask_Success() {
        TaskPatchRequest request = new TaskPatchRequest();
        Task updatedTask = new Task();
        updatedTask.setId(1L);

        when(taskService.patchUpdateTask(1L, request)).thenReturn(updatedTask);

        ResponseEntity<ApiResponse<String>> response = taskController.patchUpdateTask(1L, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Task 1 updated successfully", response.getBody().getData());
    }

    @Test
    void testGetAllTaskByUserId_Success() {
        String authorizationHeader = "Bearer testToken";
        String userName = "testuser";
        User user = new User();
        user.setId(1L);
        Page<Task> tasks = new PageImpl<>(new ArrayList<>());

        when(jwtService.extractUserName(any())).thenReturn(userName);
        when(userService.findByUsername(userName)).thenReturn(Optional.of(user));
        when(taskService.findAllTaskByUserId(1L, 0, 10)).thenReturn(tasks);

        ResponseEntity<ApiResponse<Page<Task>>> response = taskController.getAllTaskByUserId(authorizationHeader, 0, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(tasks, response.getBody().getData());
    }

    @Test
    void testGetTaskByTaskId_Success() {
        Task task = new Task();
        task.setId(1L);

        when(taskService.getTaskByTaskId(1L)).thenReturn(task);

        ResponseEntity<ApiResponse<Task>> response = taskController.getTaskByTaskId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(task, response.getBody().getData());
    }

    @Test
    void testGetTaskByDateAndUser_Success() {
        String authorizationHeader = "Bearer testToken";
        String userName = "testuser";
        User user = new User();
        user.setId(1L);
        LocalDate localDate = LocalDate.now();
        List<Task> tasks = new ArrayList<>();

        when(jwtService.extractUserName(any())).thenReturn(userName);
        when(userService.findByUsername(userName)).thenReturn(Optional.of(user));
        when(taskService.getTaskByDueDateAndUser(localDate, user)).thenReturn(tasks);

        ResponseEntity<ApiResponse<List<Task>>> response = taskController.getTaskByDateAndUser(authorizationHeader, localDate);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(tasks, response.getBody().getData());
    }

    @Test
    void testGetAllTaskByUserIdWithFilters_Success() {
        String authorizationHeader = "Bearer testToken";
        String userName = "testuser";
        User user = new User();
        user.setId(1L);
        Page<Task> tasks = new PageImpl<>(new ArrayList<>());

        when(jwtService.extractUserName(any())).thenReturn(userName);
        when(userService.findByUsername(userName)).thenReturn(Optional.of(user));
        when(taskService.findAllTaskByUserId(1L, 0, 10, null, "ASC")).thenReturn(tasks);

        ResponseEntity<ApiResponse<Page<Task>>> response = taskController.getAllTaskByUserId(authorizationHeader, 0, 10, null, "ASC");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(tasks, response.getBody().getData());
    }
}