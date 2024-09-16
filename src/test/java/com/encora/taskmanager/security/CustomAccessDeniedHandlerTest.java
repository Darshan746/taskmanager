package com.encora.taskmanager.security;

import com.encora.taskmanager.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomAccessDeniedHandlerTest {

    @InjectMocks
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request; // Mock request

    @Mock
    private HttpServletResponse response; // Mock response

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleAccessDeniedException() throws IOException, ServletException {
        AccessDeniedException accessDeniedException = new AccessDeniedException("Access Denied");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        customAccessDeniedHandler.handle(request, response, accessDeniedException);

        verify(objectMapper).writeValue(writer, new ApiResponse<>(null, "Access Denied"));
        verify(response).setStatus(HttpStatus.FORBIDDEN.value());
        verify(response).setContentType("application/json");
    }
}