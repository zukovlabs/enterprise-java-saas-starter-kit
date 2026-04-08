package com.saaskit.starter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saaskit.starter.model.Customer;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
    private CustomerRepository customerRepository;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private DataSource dataSource;

    @Test
    void shouldReturnAllCustomers() throws Exception {
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setEmail("elon@tesla.com");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setEmail("jeff@amazon.com");

        List<Customer> mockCustomers = Arrays.asList(customer1, customer2);

        when(customerRepository.findAll()).thenReturn(mockCustomers);

        mockMvc.perform(get("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("elon@tesla.com"))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void shouldCreateCustomerSuccessfully() throws Exception {
        Customer requestCustomer = new Customer();
        requestCustomer.setEmail("bill@microsoft.com");

        Customer savedCustomer = new Customer();
        savedCustomer.setId(3L);
        savedCustomer.setEmail("bill@microsoft.com");

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.email").value("bill@microsoft.com"));

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void shouldDeleteCustomerSuccessfully() throws Exception {
        doNothing().when(customerRepository).deleteById(1L);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isOk());

        verify(customerRepository, times(1)).deleteById(1L);
    }
}