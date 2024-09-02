package com.encora.taskmanager.exceptionHandlers;

import com.encora.taskmanager.response.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        final Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(new ApiResponse<>(null, errors.toString()));
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiResponse<Object>> UserExistException(UserAlreadyExistException ex) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(null, ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(Exception ex) {
        return ResponseEntity.status(403).body(new ApiResponse<>(null, ex.getMessage()));
    }

    @ExceptionHandler(TaskDeleteException.class)
    public ResponseEntity<ApiResponse<Object>> taskDeletionException(Exception ex) {
        return ResponseEntity.status(500).body(new ApiResponse<>(null, "unable to delete the Task: "+ex.getMessage()));
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> taskNotFoundException(Exception ex) {
        return ResponseEntity.status(404).body(new ApiResponse<>(null, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        return ResponseEntity.status(500).body(new ApiResponse<>(null, ex.getMessage()));
    }
}