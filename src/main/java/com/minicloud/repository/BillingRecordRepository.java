package com.minicloud.repository;

import com.minicloud.model.BillingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BillingRecordRepository extends JpaRepository<BillingRecord, Long> {
    List<BillingRecord> findByOwner(String owner);
}
