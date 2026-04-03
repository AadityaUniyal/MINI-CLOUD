package com.minicloud.backend.controller;

import com.minicloud.backend.model.CfStack;
import com.minicloud.backend.service.CloudFormationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cloudformation")
public class CloudFormationController {

    private final CloudFormationService cfService;

    public CloudFormationController(CloudFormationService cfService) {
        this.cfService = cfService;
    }

    @PostMapping("/stack")
    public ResponseEntity<CfStack> createStack(@RequestBody Map<String, String> request, Principal principal) {
        return ResponseEntity.ok(cfService.createStack(
                principal.getName(),
                request.get("stackName"),
                request.get("template")
        ));
    }

    @GetMapping("/stacks")
    public List<CfStack> listStacks(Principal principal) {
        return cfService.getStacksByOwner(principal.getName());
    }

    @DeleteMapping("/stack/{stackName}")
    public ResponseEntity<Void> deleteStack(@PathVariable String stackName) {
        cfService.deleteStack(stackName);
        return ResponseEntity.ok().build();
    }
}
