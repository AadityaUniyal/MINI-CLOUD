package com.minicloud.compute.controller;

import com.minicloud.compute.dto.CreateInstanceRequest;
import com.minicloud.common.dto.InstanceResponse;
import com.minicloud.compute.model.Instance;
import com.minicloud.compute.service.InstanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/instances")
public class InstanceController {

    private final InstanceService instanceService;

    public InstanceController(InstanceService instanceService) {
        this.instanceService = instanceService;
    }

    @GetMapping
    public ResponseEntity<List<InstanceResponse>> listInstances(@RequestParam String tenantId) {
        List<InstanceResponse> response = instanceService.getAllInstances(tenantId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<InstanceResponse> createInstance(@RequestBody CreateInstanceRequest request) {
        Instance instance = instanceService.createInstance(request.getTenantId(), request.getName(), request.getType(), request.getImageId());
        return ResponseEntity.ok(mapToResponse(instance));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Void> startInstance(@PathVariable String id) {
        instanceService.startInstance(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<Void> stopInstance(@PathVariable String id) {
        instanceService.stopInstance(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> terminateInstance(@PathVariable String id) {
        instanceService.terminateInstance(id);
        return ResponseEntity.ok().build();
    }

    private InstanceResponse mapToResponse(Instance i) {
        return InstanceResponse.builder()
                .instanceId(i.getInstanceId())
                .tenantId(i.getTenantId())
                .name(i.getName())
                .type(i.getType())
                .state(i.getState())
                .ipAddress(i.getIpAddress())
                .imageId(i.getImageId())
                .createdAt(i.getCreatedAt())
                .build();
    }
}
