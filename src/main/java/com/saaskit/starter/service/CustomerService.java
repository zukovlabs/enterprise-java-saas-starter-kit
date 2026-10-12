package com.saaskit.starter.service;

import com.saaskit.starter.dto.CustomerRequest;
import com.saaskit.starter.dto.CustomerResponse;
import com.saaskit.starter.exception.DuplicateResourceException;
import com.saaskit.starter.exception.ResourceNotFoundException;
import com.saaskit.starter.model.Customer;
import com.saaskit.starter.repository.CustomerRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    static final String DEFAULT_STATUS = "New";

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Explicit ordering: PostgreSQL does not guarantee row order, so updated rows would otherwise move around
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll(Sort.by("id")).stream()
                .map(CustomerResponse::from)
                .toList();
    }

    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw duplicateEmail(request.email());
        }
        Customer customer = new Customer();
        applyRequest(customer, request);
        return CustomerResponse.from(customerRepository.save(customer));
    }

    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        if (customerRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw duplicateEmail(request.email());
        }
        applyRequest(customer, request);
        return CustomerResponse.from(customerRepository.save(customer));
    }

    @Transactional
    public void delete(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer", id);
        }
        customerRepository.deleteById(id);
    }

    private static void applyRequest(Customer customer, CustomerRequest request) {
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setCompany(request.company());
        customer.setStatus(request.status() != null ? request.status() : DEFAULT_STATUS);
    }

    private static DuplicateResourceException duplicateEmail(String email) {
        return new DuplicateResourceException("Customer with email " + email + " already exists");
    }
}
