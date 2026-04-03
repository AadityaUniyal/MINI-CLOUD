package com.minicloud.billing.repository;

import com.minicloud.billing.model.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsageRecordRepository extends JpaRepository<UsageRecord, Long> {
    List<UsageRecord> findByTenantId(String tenantId);
}
