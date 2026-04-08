package com.minicloud.loadbalancer.controller;

import com.minicloud.loadbalancer.model.LoadBalancer;
import com.minicloud.loadbalancer.service.LoadBalancerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Fix #10: REST endpoints for load balancer management.
 */
@RestController
@RequestMapping("/api/v1/loadbalancers")
public class LoadBalancerController {

    private final LoadBalancerService loadBalancerService;

    public LoadBalancerController(LoadBalancerService loadBalancerService) {
        this.loadBalancerService = loadBalancerService;
    }

    /**
     * POST /api/v1/loadbalancers
     * Creates a new load balancer for a tenant.
     *
     * Body: { "tenantId": "...", "name": "my-lb", "type": "APPLICATION" }
     */
    @PostMapping
    public ResponseEntity<LoadBalancer> create(@RequestBody Map<String, String> body) {
        LoadBalancer lb = loadBalancerService.createLoadBalancer(
                body.get("tenantId"),
                body.get("name"),
                body.getOrDefault("type", "APPLICATION")
        );
        return ResponseEntity.ok(lb);
    }

    /**
     * GET /api/v1/loadbalancers/{tenantId}
     * Returns all load balancers belonging to a tenant.
     */
    @GetMapping("/{tenantId}")
    public ResponseEntity<List<LoadBalancer>> list(@PathVariable String tenantId) {
        return ResponseEntity.ok(loadBalancerService.getTenantLoadBalancers(tenantId));
    }
}
