package com.minicloud.controller;

import com.minicloud.dto.LaunchResponse;
import com.minicloud.service.DockerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compute")
public class ComputeController {

    private final DockerService dockerService;

    public ComputeController(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    @PostMapping("/launch")
    public ResponseEntity<LaunchResponse> launchInstance() {
        String instanceId = dockerService.launchTomcatContainer();
        return ResponseEntity.ok(LaunchResponse.builder()
                .instanceId(instanceId)
                .status("RUNNING")
                .build());
    }
}
