package com.saaskit.starter.repository;

import com.saaskit.starter.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    long countByStatus(String status);
}