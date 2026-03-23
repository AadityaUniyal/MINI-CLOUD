package com.minicloud.controller;

import com.minicloud.model.ComputeInstance;
import com.minicloud.service.ComputeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compute")
public class ComputeController {

    private final ComputeService computeService;

    public ComputeController(ComputeService computeService) {
        this.computeService = computeService;
    }

    @PostMapping("/launch")
    public ResponseEntity<ComputeInstance> launchInstance(@RequestParam String name) {
        // In a real app, user details would be taken from the SecurityContext
        ComputeInstance instance = computeService.launchInstance("admin", name);
        return ResponseEntity.ok(instance);
    }
}
