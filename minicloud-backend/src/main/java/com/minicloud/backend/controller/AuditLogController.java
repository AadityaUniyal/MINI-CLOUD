package com.minicloud.backend.controller;

import com.minicloud.backend.model.AuditLog;
import com.minicloud.backend.model.User;
import com.minicloud.backend.repository.AuditLogRepository;
import com.minicloud.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;
    private final AuthService authService;

    public AuditLogController(AuditLogRepository auditLogRepository, AuthService authService) {
        this.auditLogRepository = auditLogRepository;
        this.authService = authService;
    }

    @GetMapping("/logs")
    public ResponseEntity<List<AuditLog>> getLogs(@RequestHeader("Authorization") String token) {
        User user = authService.getUserFromToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(auditLogRepository.findByUserOrderByTimestampDesc(user));
    }
}
