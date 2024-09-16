package com.encora.taskmanager.security;

import com.encora.taskmanager.dao.UserDao;
import com.encora.taskmanager.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserDetailServiceImplTest {

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @Mock
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRoles("USER");

        when(userDao.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userDao.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailService.loadUserByUsername("nonexistentuser"));
    }

   // @Test
    void testGetRoles_WithRoles() {
        User user = new User();
        user.setRoles("USER");
        String[] roles = userDetailService.loadUserByUsername(user.getUsername()).getAuthorities().toArray(new String[0]);
        assertEquals(2, roles.length);
        assertEquals("USER", roles[0]);
        //assertEquals("ADMIN", roles[1]);
    }

    //@Test
    void testGetRoles_WithoutRoles() {
        User user = new User();
        when(userDetailService.loadUserByUsername(anyString())).thenReturn((UserDetails) userDetailService);
        String[] roles = userDetailService.loadUserByUsername(user.getUsername()).getAuthorities().toArray(new String[0]);
        assertEquals(1, roles.length);
        assertEquals("USER", roles[0]);
    }
}
