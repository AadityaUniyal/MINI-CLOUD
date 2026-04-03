package com.minicloud.backend.controller;

import com.minicloud.backend.model.BackupRecord;
import com.minicloud.backend.service.BackupService;
import com.minicloud.common.dto.BackupRecordDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/backups")
public class BackupController {

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @PostMapping("/trigger")
    public ResponseEntity<BackupRecordDto> triggerBackup(
            @RequestParam String service,
            @RequestParam String engine,
            @RequestParam String db,
            @RequestParam String user,
            @RequestParam String pass) {
        
        BackupRecord record = backupService.performBackup(service, engine, db, user, pass);
        return ResponseEntity.ok(mapToDto(record));
    }

    @GetMapping("/list")
    public ResponseEntity<List<BackupRecordDto>> getBackups(@RequestParam(required = false) String service) {
        List<BackupRecordDto> list = backupService.getBackupHistory(service).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    private BackupRecordDto mapToDto(BackupRecord record) {
        return new BackupRecordDto(
                record.getId(),
                record.getFileName(),
                record.getServiceName(),
                record.getDatabaseEngine(),
                record.getStatus(),
                record.getErrorCode(),
                record.getTimestamp(),
                record.getSizeBytes()
        );
    }
}
