package com.minicloud.controller;

import com.minicloud.dto.LaunchRequest;
import com.minicloud.model.ComputeInstance;
import com.minicloud.service.ComputeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/compute")
public class ComputeController {

    private final ComputeService computeService;

    public ComputeController(ComputeService computeService) {
        this.computeService = computeService;
    }

    @PostMapping("/launch")
    public ResponseEntity<ComputeInstance> launchInstance(@RequestBody LaunchRequest request, Principal principal) {
        ComputeInstance instance = computeService.launchInstance(
            principal.getName(), 
            request.getInstanceName(),
            request.getVpcId(),
            request.getSubnetId(),
            request.getAmiId()
        );
        return ResponseEntity.ok(instance);
    }

    @GetMapping("/list")
    public List<ComputeInstance> listInstances(Authentication auth) {
        return computeService.getInstancesByOwner(auth.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> terminateInstance(@PathVariable Long id, Authentication auth) {
        computeService.terminateInstance(id, auth.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<Void> stopInstance(@PathVariable Long id, Authentication auth) {
        computeService.stopInstance(id, auth.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Void> startInstance(@PathVariable Long id, Authentication auth) {
        computeService.startInstance(id, auth.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/terminate")
    public ResponseEntity<Void> terminateInstance(@RequestParam String name) {
        computeService.terminateInstance(name);
        return ResponseEntity.ok().build();
    }
}
