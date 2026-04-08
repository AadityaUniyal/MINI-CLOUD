package com.minicloud.billing.controller;

import com.minicloud.billing.model.UsageRecord;
import com.minicloud.billing.service.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Fix #9: REST endpoints that the API Gateway routes /api/v1/billing/** to.
 */
@RestController
@RequestMapping("/api/v1/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    /**
     * GET /api/v1/billing/usage/{tenantId}
     * Returns all usage records for a given tenant.
     */
    @GetMapping("/usage/{tenantId}")
    public ResponseEntity<List<UsageRecord>> getUsage(@PathVariable String tenantId) {
        return ResponseEntity.ok(billingService.getTenantUsage(tenantId));
    }

    /**
     * POST /api/v1/billing/record
     * Records a usage event for a resource.
     *
     * Body: { "tenantId": "...", "resourceType": "COMPUTE", "resourceId": "...", "quantity": 1.0 }
     */
    @PostMapping("/record")
    public ResponseEntity<Void> recordUsage(@RequestBody Map<String, Object> body) {
        String tenantId    = (String) body.get("tenantId");
        String resourceType = (String) body.get("resourceType");
        String resourceId  = (String) body.get("resourceId");
        Double quantity    = Double.parseDouble(body.getOrDefault("quantity", 1.0).toString());
        billingService.recordUsage(tenantId, resourceType, resourceId, quantity);
        return ResponseEntity.accepted().build();
    }
}
