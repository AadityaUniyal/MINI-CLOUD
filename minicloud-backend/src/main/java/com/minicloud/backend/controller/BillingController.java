package com.minicloud.backend.controller;

import com.minicloud.backend.service.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getBillingSummary(Principal principal) {
        String owner = principal.getName();
        return ResponseEntity.ok(Map.of(
            "totalCost", billingService.getTotalCost(owner),
            "byService", billingService.getCostByService(owner)
        ));
    }

    @PostMapping("/generate-mock")
    public ResponseEntity<Void> triggerMockBilling() {
        billingService.generateBillingRecords();
        return ResponseEntity.ok().build();
    }
}
