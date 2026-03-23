package com.minicloud.controller;

import com.minicloud.dto.DeployStackRequest;
import com.minicloud.dto.DeployStackResponse;
import com.minicloud.service.OrchestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orchestration")
public class OrchestrationController {

    private final OrchestrationService orchestrationService;

    public OrchestrationController(OrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    @PostMapping("/deploy")
    public ResponseEntity<DeployStackResponse> deployStack(@RequestBody DeployStackRequest request) {
        DeployStackResponse response = orchestrationService.deployFullStack("admin", request);
        return ResponseEntity.ok(response);
    }
}
