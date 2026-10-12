package com.saaskit.starter.service;

import com.saaskit.starter.dto.DashboardStats;
import com.saaskit.starter.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    static final String ACTIVE_STATUS = "Active";

    private final CustomerRepository customerRepository;

    public DashboardService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public DashboardStats getStats() {
        return new DashboardStats(
                customerRepository.count(),
                customerRepository.countByStatus(ACTIVE_STATUS)
        );
    }
}
