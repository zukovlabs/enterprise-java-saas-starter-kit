package com.saaskit.starter.dto;

import com.saaskit.starter.model.Customer;

public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String company,
        String status
) {
    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getCompany(),
                customer.getStatus()
        );
    }
}
