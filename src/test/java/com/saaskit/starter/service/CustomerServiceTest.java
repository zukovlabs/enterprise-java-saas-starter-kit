package com.saaskit.starter.service;

import com.saaskit.starter.dto.CustomerRequest;
import com.saaskit.starter.dto.CustomerResponse;
import com.saaskit.starter.exception.DuplicateResourceException;
import com.saaskit.starter.exception.ResourceNotFoundException;
import com.saaskit.starter.model.Customer;
import com.saaskit.starter.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldReturnCustomersOrderedById() {
        when(customerRepository.findAll(Sort.by("id"))).thenReturn(List.of(
                customer(1L, "elon@tesla.com", "Active"),
                customer(2L, "jeff@amazon.com", "Lead")
        ));

        List<CustomerResponse> result = customerService.findAll();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("jeff@amazon.com", result.get(1).email());
    }

    @Test
    void shouldCreateCustomer() {
        CustomerRequest request = new CustomerRequest("Bill", "Gates", "bill@microsoft.com", "Microsoft", "Lead");
        when(customerRepository.existsByEmail("bill@microsoft.com")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer saved = invocation.getArgument(0);
            saved.setId(3L);
            return saved;
        });

        CustomerResponse result = customerService.create(request);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(captor.capture());
        Customer saved = captor.getValue();
        assertEquals("Bill", saved.getFirstName());
        assertEquals("Gates", saved.getLastName());
        assertEquals("bill@microsoft.com", saved.getEmail());
        assertEquals("Microsoft", saved.getCompany());
        assertEquals("Lead", saved.getStatus());
        assertEquals(3L, result.id());
    }

    @Test
    void shouldDefaultStatusToNewWhenNotProvided() {
        CustomerRequest request = new CustomerRequest("Bill", "Gates", "bill@microsoft.com", null, null);
        when(customerRepository.existsByEmail("bill@microsoft.com")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerResponse result = customerService.create(request);

        assertEquals(CustomerService.DEFAULT_STATUS, result.status());
    }

    @Test
    void shouldRejectCreateWhenEmailAlreadyExists() {
        CustomerRequest request = new CustomerRequest("Elon", "Musk", "elon@tesla.com", "Tesla", "Active");
        when(customerRepository.existsByEmail("elon@tesla.com")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> customerService.create(request));

        assertEquals("Customer with email elon@tesla.com already exists", exception.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void shouldUpdateCustomer() {
        Customer existing = customer(2L, "jeff@amazon.com", "Lead");
        CustomerRequest request = new CustomerRequest("Jeff", "Bezos", "jeff@blueorigin.com", "Blue Origin", "Active");
        when(customerRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(customerRepository.existsByEmailAndIdNot("jeff@blueorigin.com", 2L)).thenReturn(false);
        when(customerRepository.save(existing)).thenReturn(existing);

        CustomerResponse result = customerService.update(2L, request);

        assertEquals(2L, result.id());
        assertEquals("jeff@blueorigin.com", existing.getEmail());
        assertEquals("Blue Origin", existing.getCompany());
        assertEquals("Active", existing.getStatus());
        verify(customerRepository).save(existing);
    }

    @Test
    void shouldAllowUpdateKeepingOwnEmail() {
        Customer existing = customer(2L, "jeff@amazon.com", "Lead");
        CustomerRequest request = new CustomerRequest("Jeff", "Bezos", "jeff@amazon.com", "Amazon", "Active");
        when(customerRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(customerRepository.existsByEmailAndIdNot("jeff@amazon.com", 2L)).thenReturn(false);
        when(customerRepository.save(existing)).thenReturn(existing);

        CustomerResponse result = customerService.update(2L, request);

        assertEquals("Active", result.status());
    }

    @Test
    void shouldThrowNotFoundWhenUpdatingMissingCustomer() {
        CustomerRequest request = new CustomerRequest("A", "B", "a@b.com", null, null);
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerService.update(99L, request));

        assertEquals("Customer with id 99 not found", exception.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void shouldRejectUpdateWhenEmailBelongsToAnotherCustomer() {
        Customer existing = customer(2L, "jeff@amazon.com", "Lead");
        CustomerRequest request = new CustomerRequest("Jeff", "Bezos", "elon@tesla.com", "Amazon", "Lead");
        when(customerRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(customerRepository.existsByEmailAndIdNot("elon@tesla.com", 2L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> customerService.update(2L, request));

        assertEquals("jeff@amazon.com", existing.getEmail());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void shouldDeleteExistingCustomer() {
        when(customerRepository.existsById(1L)).thenReturn(true);

        customerService.delete(1L);

        verify(customerRepository).deleteById(1L);
    }

    @Test
    void shouldThrowNotFoundWhenDeletingMissingCustomer() {
        when(customerRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> customerService.delete(99L));

        verify(customerRepository, never()).deleteById(any());
    }

    private static Customer customer(Long id, String email, String status) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstName("First");
        customer.setLastName("Last");
        customer.setEmail(email);
        customer.setStatus(status);
        return customer;
    }
}
