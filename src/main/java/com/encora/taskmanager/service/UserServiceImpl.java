package com.encora.taskmanager.service;

import com.encora.taskmanager.dao.UserDao;
import com.encora.taskmanager.entity.User;
import com.encora.taskmanager.exceptionHandlers.UserAlreadyExistException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Override
    public User createUser(User user) {
        Optional<User> existingUser = userDao.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException("User with name " + user.getUsername() + " already Exist");
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return userDao.save(user);
        }
    }

    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
