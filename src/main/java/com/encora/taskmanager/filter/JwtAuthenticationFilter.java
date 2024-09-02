package com.encora.taskmanager.filter;

import com.encora.taskmanager.constant.StringConstants;
import com.encora.taskmanager.jwt.JwtService;
import com.encora.taskmanager.response.ApiResponse;
import com.encora.taskmanager.security.UserDetailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(StringConstants.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(StringConstants.BEARER)) {
            filterChain.doFilter(request, response);
            return;
        } else {
            try {
                final String token = authHeader.substring(7);
                final String userName = jwtService.extractUserName(token);
                if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    final UserDetails userDetails = userDetailService.loadUserByUsername(userName);
                    if (userDetails != null && jwtService.isTokenExpired(token)) {
                        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, userDetails.getPassword(), userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (SignatureException e) {
                final ApiResponse<Object> errorResponse = new ApiResponse<>(null, "UnAuthorized access");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return; // Stop further processing
            } catch (MalformedJwtException e) {
                final ApiResponse<Object> errorResponse = new ApiResponse<>(null, "Invalid JWT Token");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return; // Stop further processing
            }
            catch (ExpiredJwtException e) {
                final ApiResponse<Object> errorResponse = new ApiResponse<>(null, "JWT Token expired");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return; // Stop further processing
            }
        }
        filterChain.doFilter(request, response);
    }
}
