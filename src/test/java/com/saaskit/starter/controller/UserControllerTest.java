package com.saaskit.starter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saaskit.starter.dto.ChangePasswordRequest;
import com.saaskit.starter.dto.UpdateProfileRequest;
import com.saaskit.starter.model.User;
import com.saaskit.starter.service.UserService;
import com.saaskit.starter.security.JwtUtils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.Authentication;

import javax.sql.DataSource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private DataSource dataSource;

    @Test
    void shouldGetProfile() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        when(userService.getUserByEmail("test@example.com")).thenReturn(mockUser);

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn("test@example.com");

        mockMvc.perform(get("/api/user/profile")
                        .principal(mockAuthentication)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(userService, times(1)).getUserByEmail("test@example.com");
    }

    @Test
    void shouldUpdateProfile() throws Exception {
        UpdateProfileRequest request = new UpdateProfileRequest("Jane", "Doe");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("test@example.com");
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Doe");

        when(userService.updateProfile(eq("test@example.com"), any(UpdateProfileRequest.class)))
                .thenReturn(updatedUser);

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn("test@example.com");

        mockMvc.perform(put("/api/user/profile")
                        .principal(mockAuthentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(userService, times(1)).updateProfile(eq("test@example.com"), any(UpdateProfileRequest.class));
    }

    @Test
    void shouldChangePassword() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "newPass123");

        doNothing().when(userService).changePassword(eq("test@example.com"), any(ChangePasswordRequest.class));

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn("test@example.com");

        mockMvc.perform(put("/api/user/password")
                        .principal(mockAuthentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userService, times(1)).changePassword(eq("test@example.com"), any(ChangePasswordRequest.class));
    }
}