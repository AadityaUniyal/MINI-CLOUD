package com.minicloud.backend.service;

import com.minicloud.backend.model.AuditLog;
import com.minicloud.backend.model.User;
import com.minicloud.backend.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class CloudTrailService {

    private final AuditLogRepository auditLogRepository;

    public CloudTrailService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logEvent(User user, String action, String resourceId, String details, String ip, String ua, String params) {
        AuditLog log = AuditLog.builder()
                .user(user)
                .action(action)
                .resourceId(resourceId)
                .details(details)
                .timestamp(LocalDateTime.now())
                .sourceIp(ip != null ? ip : "127.0.0.1")
                .userAgent(ua != null ? ua : "MiniCloudConsole/1.0")
                .requestParameters(params)
                .build();
        auditLogRepository.save(log);
    }
}
