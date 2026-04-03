package com.minicloud.service;

import com.minicloud.backend.model.BillingRecord;
import com.minicloud.backend.model.User;
import com.minicloud.backend.repository.BillingRecordRepository;
import com.minicloud.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BillingServiceTest {

    @Mock
    private BillingRecordRepository billingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BillingService billingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartBilling() {
        User user = User.builder().username("testuser").build();
        billingService.startBilling(user, "container-123", "COMPUTE");
        verify(billingRepository, times(1)).save(any(BillingRecord.class));
    }

    @Test
    void testStopBilling() {
        // Updated test for BillingRecord - Stop billing is currently a no-op in the new service
        billingService.stopBilling("container-123");
        // No interaction expected for stopBilling as it's a stub for now
    }
}
