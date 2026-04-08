package com.minicloud.backend.service;

import com.minicloud.backend.model.ComputeInstance;
import com.minicloud.backend.model.DatabaseInstance;
import com.minicloud.backend.repository.ComputeInstanceRepository;
import com.minicloud.backend.repository.DatabaseInstanceRepository;
import com.minicloud.backend.service.BillingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillingScheduler {

    private static final Logger log = LoggerFactory.getLogger(BillingScheduler.class);
    private final ComputeInstanceRepository computeInstanceRepository;
    private final DatabaseInstanceRepository databaseInstanceRepository;
    private final BillingService billingService;
    
    private static final double MINUTE_RATE = 0.05 / 60.0; // $0.05 per hour / 60 minutes

    public BillingScheduler(ComputeInstanceRepository computeInstanceRepository,
                            DatabaseInstanceRepository databaseInstanceRepository,
                            BillingService billingService) {
        this.computeInstanceRepository = computeInstanceRepository;
        this.databaseInstanceRepository = databaseInstanceRepository;
        this.billingService = billingService;
    }

    @Scheduled(fixedRate = 60000) // Every minute
    public void processOngoingBilling() {
        log.info("BillingScheduler: Processing ongoing resource costs...");

        // 1. Process Running Compute Instances
        List<ComputeInstance> activeInstances = computeInstanceRepository.findAll().stream()
                .filter(inst -> "RUNNING".equalsIgnoreCase(inst.getStatus()))
                .toList();
        
        for (ComputeInstance inst : activeInstances) {
            billingService.deductOngoingCosts(inst.getOwner(), MINUTE_RATE, inst.getContainerId(), "COMPUTE");
        }

        // 2. Process Running Database Instances
        List<DatabaseInstance> activeDbs = databaseInstanceRepository.findAll().stream()
                .filter(db -> "RUNNING".equalsIgnoreCase(db.getStatus()))
                .toList();

        for (DatabaseInstance db : activeDbs) {
            billingService.deductOngoingCosts(db.getOwner(), MINUTE_RATE, db.getContainerId(), "DATABASE");
        }

        log.info("BillingScheduler: Deducting costs for {} instances and {} databases.", activeInstances.size(), activeDbs.size());
    }
}
