package com.encora.taskmanager.security;

import com.encora.taskmanager.constant.StringConstants;
import com.encora.taskmanager.dao.UserDao;
import com.encora.taskmanager.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDao.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
           return org.springframework.security.core.userdetails.User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(getRoles(userObj))
                    .build();
        } else {
            throw new UsernameNotFoundException("user doesn't exist");

        }
    }

    private String[] getRoles(final User userObj) {
        if (userObj.getRoles() == null) {
            return new String[]{StringConstants.USER_ROLE};
        } else {
            return userObj.getRoles().split(",");
        }
    }
}
