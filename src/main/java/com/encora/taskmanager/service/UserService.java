package com.encora.taskmanager.service;

import com.encora.taskmanager.entity.User;

import java.util.Optional;


public interface UserService {
    User createUser(User user);
    Optional<User> findById(long id);
    Optional<User> findByUsername(String username);
}
