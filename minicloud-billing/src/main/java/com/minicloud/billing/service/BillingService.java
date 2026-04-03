package com.minicloud.billing.service;

import com.minicloud.billing.model.UsageRecord;
import com.minicloud.billing.repository.UsageRecordRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillingService {

    private final UsageRecordRepository usageRecordRepository;

    public BillingService(UsageRecordRepository usageRecordRepository) {
        this.usageRecordRepository = usageRecordRepository;
    }

    public void recordUsage(String tenantId, String resourceType, String resourceId, Double quantity) {
        UsageRecord record = UsageRecord.builder()
                .tenantId(tenantId)
                .resourceType(resourceType)
                .resourceId(resourceId)
                .quantity(quantity)
                .timestamp(LocalDateTime.now())
                .build();
        usageRecordRepository.save(record);
    }

    public List<UsageRecord> getTenantUsage(String tenantId) {
        return usageRecordRepository.findByTenantId(tenantId);
    }
}
