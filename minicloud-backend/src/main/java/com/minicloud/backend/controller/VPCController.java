package com.minicloud.backend.controller;

import com.minicloud.common.dto.VpcRequest;
import com.minicloud.backend.model.VPC;
import com.minicloud.backend.service.networking.VPCService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/vpc")
public class VPCController {

    private final VPCService vpcService;

    public VPCController(VPCService vpcService) {
        this.vpcService = vpcService;
    }

    @PostMapping("/create")
    public ResponseEntity<VPC> createVpc(@RequestBody VpcRequest request, Principal principal) {
        return ResponseEntity.ok(vpcService.createVPC(
                principal.getName(),
                request.getCidrBlock(),
                request.isEnableDnsSupport(),
                request.isEnableDnsHostnames()
        ));
    }

    @GetMapping("/list")
    public ResponseEntity<List<VPC>> listVpcs(Principal principal) {
        return ResponseEntity.ok(vpcService.describeVPCs(principal.getName()));
    }

    @DeleteMapping("/{vpcId}")
    public ResponseEntity<Void> deleteVpc(@PathVariable String vpcId, Principal principal) {
        vpcService.deleteVPC(vpcId, principal.getName());
        return ResponseEntity.ok().build();
    }
}
