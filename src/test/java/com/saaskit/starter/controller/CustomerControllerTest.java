package com.saaskit.starter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saaskit.starter.dto.CustomerRequest;
import com.saaskit.starter.dto.CustomerResponse;
import com.saaskit.starter.exception.DuplicateResourceException;
import com.saaskit.starter.exception.ResourceNotFoundException;
import com.saaskit.starter.security.JwtUtils;
import com.saaskit.starter.service.CustomerService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private DataSource dataSource;

    private static final CustomerRequest VALID_REQUEST =
            new CustomerRequest("Bill", "Gates", "bill@microsoft.com", "Microsoft", "Active");

    @Test
    void shouldReturnAllCustomers() throws Exception {
        when(customerService.findAll()).thenReturn(List.of(
                new CustomerResponse(1L, "Elon", "Musk", "elon@tesla.com", "Tesla", "Active"),
                new CustomerResponse(2L, "Jeff", "Bezos", "jeff@amazon.com", "Amazon", "Lead")
        ));

        mockMvc.perform(get("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("elon@tesla.com"))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(customerService, times(1)).findAll();
    }

    @Test
    void shouldCreateCustomerAndReturn201WithLocation() throws Exception {
        when(customerService.create(any(CustomerRequest.class))).thenReturn(
                new CustomerResponse(3L, "Bill", "Gates", "bill@microsoft.com", "Microsoft", "Active"));

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(VALID_REQUEST)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/customers/3"))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.email").value("bill@microsoft.com"));

        verify(customerService, times(1)).create(VALID_REQUEST);
    }

    @Test
    void shouldReturn400WithFieldErrorsWhenCreateRequestIsInvalid() throws Exception {
        CustomerRequest invalid = new CustomerRequest("", "Gates", "not-an-email", null, "Unknown");

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.errors.firstName").value("First name is required"))
                .andExpect(jsonPath("$.errors.email").value("Invalid email format"))
                .andExpect(jsonPath("$.errors.status").exists());

        verify(customerService, never()).create(any());
    }

    @Test
    void shouldReturn409WhenCreatingCustomerWithDuplicateEmail() throws Exception {
        when(customerService.create(any(CustomerRequest.class)))
                .thenThrow(new DuplicateResourceException("Customer with email bill@microsoft.com already exists"));

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(VALID_REQUEST)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.detail").value("Customer with email bill@microsoft.com already exists"));
    }

    @Test
    void shouldUpdateCustomer() throws Exception {
        when(customerService.update(eq(3L), any(CustomerRequest.class))).thenReturn(
                new CustomerResponse(3L, "Bill", "Gates", "bill@microsoft.com", "Microsoft", "Active"));

        mockMvc.perform(put("/api/customers/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(VALID_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.status").value("Active"));

        verify(customerService, times(1)).update(3L, VALID_REQUEST);
    }

    @Test
    void shouldReturn404WhenUpdatingMissingCustomer() throws Exception {
        when(customerService.update(eq(99L), any(CustomerRequest.class)))
                .thenThrow(new ResourceNotFoundException("Customer", 99L));

        mockMvc.perform(put("/api/customers/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(VALID_REQUEST)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource not found"))
                .andExpect(jsonPath("$.detail").value("Customer with id 99 not found"))
                .andExpect(jsonPath("$.instance").value("/api/customers/99"));
    }

    @Test
    void shouldReturn400WhenUpdateRequestIsInvalid() throws Exception {
        CustomerRequest invalid = new CustomerRequest("Bill", "", "bill@microsoft.com", null, null);

        mockMvc.perform(put("/api/customers/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.lastName").value("Last name is required"));

        verify(customerService, never()).update(any(), any());
    }

    @Test
    void shouldDeleteCustomerAndReturn204() throws Exception {
        doNothing().when(customerService).delete(1L);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).delete(1L);
    }

    @Test
    void shouldReturn404WhenDeletingMissingCustomer() throws Exception {
        doThrow(new ResourceNotFoundException("Customer", 99L)).when(customerService).delete(99L);

        mockMvc.perform(delete("/api/customers/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Customer with id 99 not found"));
    }

    @Test
    void shouldReturn500WithoutLeakingDetailsOnUnexpectedError() throws Exception {
        when(customerService.findAll()).thenThrow(new RuntimeException("connection refused: db:5432"));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred"));
    }

    @Test
    void shouldReturn400WhenBodyIsMalformedJson() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{not json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(customerService, never()).create(any());
    }
}
