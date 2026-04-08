package com.saaskit.starter.controller;

import com.saaskit.starter.repository.CustomerRepository;
import com.saaskit.starter.security.JwtUtils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
@AutoConfigureMockMvc(addFilters = false)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private DataSource dataSource;

    @Test
    void shouldReturnDashboardStats() throws Exception {
        when(customerRepository.count()).thenReturn(15L);
        when(customerRepository.countByStatus("Active")).thenReturn(7L);

        mockMvc.perform(get("/api/dashboard/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCustomers").value(15))
                .andExpect(jsonPath("$.activeCustomers").value(7));

        verify(customerRepository, times(1)).count();
        verify(customerRepository, times(1)).countByStatus("Active");
    }
}