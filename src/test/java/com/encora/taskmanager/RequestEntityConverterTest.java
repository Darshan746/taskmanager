package com.encora.taskmanager;

import com.encora.taskmanager.converter.RequestToEntityConverter;
import com.encora.taskmanager.dao.UserDao;
import com.encora.taskmanager.dto.request.TaskCreateRequest;
import com.encora.taskmanager.dto.request.UserSignUpRequest;
import com.encora.taskmanager.entity.Task;
import com.encora.taskmanager.entity.User;
import com.encora.taskmanager.enumconstants.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RequestToEntityConverterTest {

    @InjectMocks
    private RequestToEntityConverter requestToEntityConverter;

    @Mock
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertUserRequestToUserEntity() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest();
        userSignUpRequest.setName("testuser");
        userSignUpRequest.setEmail("test@example.com");
        userSignUpRequest.setPassword("password");
        userSignUpRequest.setRoles("USER");

        User expectedUser = new User();
        expectedUser.setUsername("testuser");
        expectedUser.setEmail("test@example.com");
        expectedUser.setPassword("password");
        expectedUser.setRoles("USER");

        User actualUser = requestToEntityConverter.convertUserRequestToUserEntity(userSignUpRequest);

        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        assertEquals(expectedUser.getRoles(), actualUser.getRoles());
    }

    @Test
    void testConvertTaskCreateRequestToTaskEntity() {
        TaskCreateRequest taskCreateRequest = new TaskCreateRequest();
        taskCreateRequest.setTaskTitle("Test Task");
        taskCreateRequest.setTaskDescription("Test Description");
        taskCreateRequest.setTaskDueDate(LocalDate.now());
        taskCreateRequest.setTaskStatus(TaskStatus.valueOf(taskCreateRequest.getTaskStatus().name()));
        taskCreateRequest.setUserId(1L);

        User user = new User();
        user.setId(1L);
        when(userDao.findById(1L)).thenReturn(java.util.Optional.of(user));

        Task expectedTask = new Task();
        expectedTask.setTaskTitle("Test Task");
        expectedTask.setTaskDescription("Test Description");
        expectedTask.setTaskDueDate(LocalDate.now());
        expectedTask.setTaskStatus(TaskStatus.valueOf(taskCreateRequest.getTaskStatus().name()));
        expectedTask.setUser(user);

        Task actualTask = requestToEntityConverter.convertTaskCreateRequestToTaskEntity(taskCreateRequest, user);

        assertEquals(expectedTask.getTaskTitle(), actualTask.getTaskTitle());
        assertEquals(expectedTask.getTaskDescription(), actualTask.getTaskDescription());
        assertEquals(expectedTask.getTaskDueDate(), actualTask.getTaskDueDate());
        assertEquals(expectedTask.getTaskStatus(), actualTask.getTaskStatus());
        assertEquals(expectedTask.getUser(), actualTask.getUser());
    }
}