package com.minicloud.backend.service;

import com.minicloud.backend.model.BillingRecord;
import com.minicloud.backend.repository.BillingRecordRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.minicloud.backend.repository.ComputeInstanceRepository;
import com.minicloud.backend.repository.DatabaseInstanceRepository;

@Service
public class BillingService {

    private final BillingRecordRepository billingRepository;
    private final ComputeInstanceRepository computeInstanceRepository;
    private final DatabaseInstanceRepository databaseInstanceRepository;

    // Backward compatibility (Week 1/2) - Now using BillingRecord
    public void startBilling(String username, String resId, String type) {
        createRecord(username, type, resId, 0.0); // 0 initial cost
    }

    public void stopBilling(String resId) {
        // Just record final state if needed, but scheduler handles ongoing costs
    }

    public BillingService(BillingRecordRepository billingRepository,
                          ComputeInstanceRepository computeInstanceRepository,
                          DatabaseInstanceRepository databaseInstanceRepository) {
        this.billingRepository = billingRepository;
        this.computeInstanceRepository = computeInstanceRepository;
        this.databaseInstanceRepository = databaseInstanceRepository;
    }

    /**
     * On-demand billing snapshot — call manually or via admin API.
     * NOTE: @Scheduled removed to prevent double-billing with BillingScheduler
     * which already runs deductOngoingCosts() every minute.
     */
    public void generateBillingRecords() {
        System.out.println("Generating hourly billing records...");
        
        // Use repositories directly to avoid circular dependency with services
        computeInstanceRepository.findAll().stream()
            .filter(inst -> "RUNNING".equals(inst.getStatus()))
            .forEach(inst -> createRecord(inst.getOwner(), "EC2", inst.getContainerId(), 0.02));

        databaseInstanceRepository.findAll().stream()
            .filter(db -> "AVAILABLE".equals(db.getStatus()))
            .forEach(db -> createRecord(db.getOwner(), "RDS", db.getName(), 0.05));
    }

    public void deductOngoingCosts(String owner, double amount, String resId, String type) {
        createRecord(owner, type, resId, amount);
    }

    private void createRecord(String owner, String type, String resId, Double amount) {
        BillingRecord record = new BillingRecord();
        record.setOwner(owner);
        record.setResourceType(type);
        record.setResourceId(resId);
        record.setAmount(amount);
        record.setCurrency("USD");
        record.setTimestamp(LocalDateTime.now());
        billingRepository.save(record);
    }

    public List<BillingRecord> getBillsByOwner(String owner) {
        return billingRepository.findByOwner(owner);
    }

    public Map<String, Double> getCostByService(String owner) {
        return getBillsByOwner(owner).stream()
            .collect(Collectors.groupingBy(
                BillingRecord::getResourceType,
                Collectors.summingDouble(BillingRecord::getAmount)
            ));
    }

    public Double getTotalCost(String owner) {
        return getBillsByOwner(owner).stream()
            .mapToDouble(BillingRecord::getAmount)
            .sum();
    }
}
