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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private RequestToEntityConverter entityConverterDTO;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/users/signup")
    public ResponseEntity<ApiResponse<String>> userSignUp(final @Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        final User user = entityConverterDTO.convertUserRequestToUserEntity(userSignUpRequest);
        final User createdUser = userService.createUser(user);
        return ResponseEntity.ok().body(new ApiResponse<>("User " + createdUser.getUsername() + " Created Successfully", null));
    }

    @GetMapping(value = "/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String greetWithAccess() {
        return "greet with access";
    }

    @PostMapping(value = "/v1/token")
    public ResponseEntity<ApiResponse<TokenResponse>> authenticateAndGenerateToken(final @RequestBody UserNamePasswordRequestDTO userNamePasswordRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userNamePasswordRequestDTO.getUsername(), userNamePasswordRequestDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(userDetailService.loadUserByUsername(userNamePasswordRequestDTO.getUsername()));
            return ResponseEntity.ok().body(new ApiResponse<>(new TokenResponse(token, "Bearer"), null));
        } else {
            throw new UsernameNotFoundException("invalid credentials");
        }
    }
}
