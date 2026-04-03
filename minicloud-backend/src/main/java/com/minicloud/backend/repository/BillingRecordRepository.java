package com.minicloud.backend.repository;

import com.minicloud.backend.model.BillingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BillingRecordRepository extends JpaRepository<BillingRecord, Long> {
    List<BillingRecord> findByOwner(String owner);
}
