package com.saaskit.starter.service;

import com.saaskit.starter.dto.DashboardStats;
import com.saaskit.starter.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void shouldAggregateTotalAndActiveCustomers() {
        when(customerRepository.count()).thenReturn(15L);
        when(customerRepository.countByStatus("Active")).thenReturn(7L);

        DashboardStats stats = dashboardService.getStats();

        assertEquals(15L, stats.totalCustomers());
        assertEquals(7L, stats.activeCustomers());
    }
}
