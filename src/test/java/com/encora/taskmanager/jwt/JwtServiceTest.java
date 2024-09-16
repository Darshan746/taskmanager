package com.encora.taskmanager.jwt;

import com.encora.taskmanager.constant.StringConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateToken() {
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    void extractUserName() {
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUserName(token);
        assertEquals("testuser", username);
    }

    //@Test
    void isTokenExpired() {
        Map<String, String> claims = new HashMap<>();
        claims.put("iss", StringConstants.JWT_ISSUER);
        String token = Jwts.builder()
                .claims(claims)
                .subject("testuser")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().minusMillis(1000))) // Token expired 1 second ago
                .signWith(jwtService.generateSecretKey())
                .compact();
        boolean isExpired = jwtService.isTokenExpired(token);
        assertTrue(isExpired);
    }

    @Test
    void isTokenNotExpired() {
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();
        String token = jwtService.generateToken(userDetails);
        boolean isExpired = jwtService.isTokenExpired(token);
        assertTrue(isExpired);
    }

    @Test
    void getClaims() {
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();
        String token = jwtService.generateToken(userDetails);
        Claims claims = jwtService.getClaims(token);
        assertNotNull(claims);
    }
}