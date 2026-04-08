package com.minicloud.backend.service;

import com.minicloud.backend.model.BillingRecord;
import com.minicloud.backend.repository.BillingRecordRepository;
import com.minicloud.backend.repository.ComputeInstanceRepository;
import com.minicloud.backend.repository.DatabaseInstanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    @Mock
    private BillingRecordRepository billingRepository;
    @Mock
    private ComputeInstanceRepository computeInstanceRepository;
    @Mock
    private DatabaseInstanceRepository databaseInstanceRepository;

    @InjectMocks
    private BillingService billingService;

    @Test
    void testDeductOngoingCosts() {
        billingService.deductOngoingCosts("user1", 0.05, "inst-123", "EC2");
        
        verify(billingRepository, times(1)).save(any(BillingRecord.class));
    }

    @Test
    void testGetTotalCost() {
        BillingRecord r1 = new BillingRecord();
        r1.setAmount(10.0);
        BillingRecord r2 = new BillingRecord();
        r2.setAmount(5.5);
        
        when(billingRepository.findByOwner("user1")).thenReturn(List.of(r1, r2));

        Double total = billingService.getTotalCost("user1");

        assertEquals(15.5, total);
    }

    @Test
    void testGenerateBillingRecords_NoInstances() {
        when(computeInstanceRepository.findAll()).thenReturn(Collections.emptyList());
        when(databaseInstanceRepository.findAll()).thenReturn(Collections.emptyList());

        billingService.generateBillingRecords();

        verify(billingRepository, never()).save(any());
    }

    @Test
    void testStartBilling() {
        // BillingService.startBilling takes (String, String, String)
        billingService.startBilling("testuser", "container-123", "COMPUTE");
        verify(billingRepository, times(1)).save(any(BillingRecord.class));
    }

    @Test
    void testStopBilling() {
        billingService.stopBilling("container-123");
        // Simple verification or just ensuring no exception
    }
}
