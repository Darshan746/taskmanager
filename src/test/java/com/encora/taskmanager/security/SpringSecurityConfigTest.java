package com.encora.taskmanager.security;

import com.encora.taskmanager.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityConfigTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Test
    void testPermitAllEndpoints() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/v1/token/**"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/signup/**"))
                .andExpect(status().isOk());
    }

       /* @Test
        @WithMockUser(roles = "ADMIN")
        void testAdminEndpoint() throws Exception {
            mockMvc.perform(get("/admin/test"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        void testAuthenticatedEndpoint() throws Exception {
            mockMvc.perform(get("/api/tasks"))
                    .andExpect(status().isOk());
        }

        @Test
        void testUnauthorizedAccess() throws Exception {
            mockMvc.perform(get("/api/tasks"))
                    .andExpect(status().isForbidden());
        }

        @Test
        void testCsrfProtection() throws Exception {
            mockMvc.perform(post("/api/tasks")
                            .with(csrf()))
                    .andExpect(status().isForbidden()); // Or another appropriate status code
        }*/

    @Test
    void testPasswordEncoder() {
        SpringSecurityConfig config = new SpringSecurityConfig();
        BCryptPasswordEncoder passwordEncoder = (BCryptPasswordEncoder) config.passwordEncoder();
        String encodedPassword = passwordEncoder.encode("password");
        assertTrue(passwordEncoder.matches("password", encodedPassword));
    }
}