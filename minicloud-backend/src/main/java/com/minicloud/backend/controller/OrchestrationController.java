package com.minicloud.backend.controller;

import com.minicloud.common.dto.DeployStackRequest;
import com.minicloud.common.dto.DeployStackResponse;
import com.minicloud.backend.model.Stack;
import com.minicloud.backend.service.OrchestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orchestration")
public class OrchestrationController {

    private final OrchestrationService orchestrationService;

    public OrchestrationController(OrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    @PostMapping("/deploy")
    public ResponseEntity<DeployStackResponse> deployStack(@RequestBody DeployStackRequest request, Principal principal) {
        DeployStackResponse response = orchestrationService.deployFullStack(principal.getName(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Stack>> listStacks(Principal principal) {
        return ResponseEntity.ok(orchestrationService.listStacks(principal.getName()));
    }

    @DeleteMapping("/{stackId}")
    public ResponseEntity<String> deleteStack(@PathVariable String stackId, Principal principal) {
        orchestrationService.deleteStack(principal.getName(), stackId);
        return ResponseEntity.ok("Stack " + stackId + " deleted successfully.");
    }

    @GetMapping("/templates")
    public ResponseEntity<Map<String, String>> getTemplates() {
        return ResponseEntity.ok(orchestrationService.getAvailableTemplates());
    }
}
