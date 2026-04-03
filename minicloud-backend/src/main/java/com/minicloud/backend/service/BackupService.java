package com.minicloud.backend.service;

import com.minicloud.backend.model.BackupRecord;
import com.minicloud.backend.repository.BackupRecordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BackupService {

    private final BackupRecordRepository backupRecordRepository;
    private final BucketService bucketService;

    @Value("${minicloud.backup.retention-days:7}")
    private int retentionDays;

    private static final String BACKUP_BUCKET = "minicloud-backups";

    public BackupService(BackupRecordRepository backupRecordRepository, BucketService bucketService) {
        this.backupRecordRepository = backupRecordRepository;
        this.bucketService = bucketService;
    }

    /**
     * Automated nightly backup (3 AM)
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void scheduleNightlyBackups() {
        performBackup("compute", "postgres", "minicloud_compute", "postgres", "postgres");
        performBackup("iam", "mysql", "minicloud_iam", "root", "password");
    }

    public BackupRecord performBackup(String serviceName, String engine, String dbName, String user, String password) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = serviceName + "_" + timestamp + ".sql";
        
        try {
            File tempFile = File.createTempFile("backup_", ".sql");
            boolean success = executeDump(engine, dbName, user, password, tempFile);
            
            if (success) {
                // Upload to S3
                try (InputStream is = new FileInputStream(tempFile)) {
                    bucketService.saveFile("system", BACKUP_BUCKET, fileName, tempFile.length(), "application/sql", is);
                }

                BackupRecord record = BackupRecord.builder()
                        .fileName(fileName)
                        .serviceName(serviceName)
                        .databaseEngine(engine)
                        .status("SUCCESS")
                        .timestamp(LocalDateTime.now())
                        .sizeBytes(tempFile.length())
                        .build();
                
                tempFile.delete();
                return backupRecordRepository.save(record);
            } else {
                throw new RuntimeException("Dump execution failed for " + dbName);
            }
        } catch (Exception e) {
            BackupRecord failedRecord = BackupRecord.builder()
                    .fileName(fileName)
                    .serviceName(serviceName)
                    .databaseEngine(engine)
                    .status("FAILED")
                    .errorCode(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return backupRecordRepository.save(failedRecord);
        }
    }

    private boolean executeDump(String engine, String dbName, String user, String password, File outputFile) throws IOException, InterruptedException {
        ProcessBuilder pb;
        if ("postgres".equalsIgnoreCase(engine)) {
            pb = new ProcessBuilder("pg_dump", "-U", user, "-d", dbName, "-f", outputFile.getAbsolutePath());
            pb.environment().put("PGPASSWORD", password);
        } else {
            pb = new ProcessBuilder("mysqldump", "-u" + user, "-p" + password, dbName, "-r", outputFile.getAbsolutePath());
        }

        Process process = pb.start();
        int exitCode = process.waitFor();
        return exitCode == 0;
    }

    public List<BackupRecord> getBackupHistory(String serviceName) {
        if (serviceName != null) {
            return backupRecordRepository.findByServiceNameOrderByTimestampDesc(serviceName);
        }
        return backupRecordRepository.findAll();
    }

    @Scheduled(cron = "0 0 2 * * SUN") // Weekly cleanup
    public void purgeOldBackups() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(retentionDays);
        List<BackupRecord> all = backupRecordRepository.findAll();
        for (BackupRecord record : all) {
            if (record.getTimestamp().isBefore(cutoff)) {
                // Delete from S3
                bucketService.deleteFile(BACKUP_BUCKET, record.getFileName());
                backupRecordRepository.delete(record);
            }
        }
    }
}
