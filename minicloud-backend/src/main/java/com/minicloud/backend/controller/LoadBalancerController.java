package com.minicloud.backend.controller;

import com.minicloud.backend.model.LoadBalancer;
import com.minicloud.backend.service.LoadBalancerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loadbalancer")
public class LoadBalancerController {

    private final LoadBalancerService loadBalancerService;

    public LoadBalancerController(LoadBalancerService loadBalancerService) {
        this.loadBalancerService = loadBalancerService;
    }

    @PostMapping("/create")
    public ResponseEntity<LoadBalancer> createLoadBalancer(@RequestBody Map<String, Object> payload, Principal principal) {
        String name = (String) payload.get("name");
        int publicPort = (int) payload.get("publicPort");
        @SuppressWarnings("unchecked")
        List<Integer> instanceIdsInt = (List<Integer>) payload.get("instanceIds");
        List<Long> instanceIds = instanceIdsInt.stream().map(Integer::longValue).toList();
        String vpcId = (String) payload.get("vpcId");
        
        LoadBalancer lb = loadBalancerService.createLoadBalancer(principal.getName(), name, publicPort, instanceIds, vpcId);
        return ResponseEntity.ok(lb);
    }

    @GetMapping("/list")
    public ResponseEntity<List<LoadBalancer>> listLoadBalancers() {
        return ResponseEntity.ok(loadBalancerService.getAllLoadBalancers());
    }

    @PostMapping("/add-instance")
    public ResponseEntity<Void> addInstance(@RequestBody Map<String, Object> payload) {
        String lbName = (String) payload.get("lbName");
        Long instanceId = Long.valueOf(payload.get("instanceId").toString());
        loadBalancerService.addInstanceToLoadBalancer(lbName, instanceId);
        return ResponseEntity.ok().build();
    }
}
