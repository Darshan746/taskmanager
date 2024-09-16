package com.encora.taskmanager.controller;

import com.encora.taskmanager.converter.RequestToEntityConverter;
import com.encora.taskmanager.dto.request.UserNamePasswordRequestDTO;
import com.encora.taskmanager.dto.request.UserSignUpRequest;
import com.encora.taskmanager.entity.User;
import com.encora.taskmanager.jwt.JwtService;
import com.encora.taskmanager.response.ApiResponse;
import com.encora.taskmanager.response.TokenResponse;
import com.encora.taskmanager.security.UserDetailServiceImpl;
import com.encora.taskmanager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailServiceImpl userDetailService;

    @Mock
    private RequestToEntityConverter entityConverterDTO;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUserSignUp() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest();
        userSignUpRequest.setName("testuser");
        userSignUpRequest.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(entityConverterDTO.convertUserRequestToUserEntity(any())).thenReturn(user);
        when(userService.createUser(any())).thenReturn(user);

        ResponseEntity<ApiResponse<String>> response = userController.userSignUp(userSignUpRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User testuser Created Successfully", response.getBody().getData());
    }

    @Test
    void testGreetWithAccess() {
        String response = userController.greetWithAccess();
        assertEquals("greet with access", response);
    }

  //  @Test
    void testAuthenticateAndGenerateToken() {
        UserNamePasswordRequestDTO requestDTO = new UserNamePasswordRequestDTO();
        requestDTO.setUsername("testuser");
        requestDTO.setPassword("password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword());
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();
        when(userDetailService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("testToken");

        ResponseEntity<ApiResponse<TokenResponse>> response = userController.authenticateAndGenerateToken(requestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testToken", response.getBody().getData().getToken());
        assertEquals("Bearer", response.getBody().getData().getTokenType());
    }
    }