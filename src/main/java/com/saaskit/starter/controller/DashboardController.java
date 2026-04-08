package com.saaskit.starter.controller;

import com.saaskit.starter.dto.DashboardStats;
import com.saaskit.starter.repository.CustomerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final CustomerRepository customerRepository;

    public DashboardController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/stats")
    public DashboardStats getStats() {
        long total = customerRepository.count();
        long active = customerRepository.countByStatus("Active");

        return new DashboardStats(total, active);
    }
}