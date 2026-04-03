package com.minicloud.backend.repository;

import com.minicloud.backend.model.BackupRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackupRecordRepository extends JpaRepository<BackupRecord, Long> {
    List<BackupRecord> findByServiceNameOrderByTimestampDesc(String serviceName);
}
