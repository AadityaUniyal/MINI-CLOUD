package com.minicloud.controller;

import com.minicloud.dto.SubnetRequest;
import com.minicloud.model.Subnet;
import com.minicloud.service.networking.SubnetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/subnet")
public class SubnetController {

    private final SubnetService subnetService;

    public SubnetController(SubnetService subnetService) {
        this.subnetService = subnetService;
    }

    @PostMapping("/create")
    public ResponseEntity<Subnet> createSubnet(@RequestBody SubnetRequest request, Principal principal) {
        return ResponseEntity.ok(subnetService.createSubnet(
                principal.getName(),
                request.getVpcId(),
                request.getCidrBlock(),
                request.getAvailabilityZone(),
                request.isMapPublicIpOnLaunch()
        ));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Subnet>> listSubnets(Principal principal) {
        return ResponseEntity.ok(subnetService.listSubnets(principal.getName()));
    }

    @GetMapping("/vpc/{vpcId}")
    public ResponseEntity<List<Subnet>> listSubnetsByVpc(@PathVariable String vpcId) {
        return ResponseEntity.ok(subnetService.getSubnetsByVpc(vpcId));
    }

    @DeleteMapping("/{subnetId}")
    public ResponseEntity<Void> deleteSubnet(@PathVariable String subnetId, Principal principal) {
        subnetService.deleteSubnet(subnetId, principal.getName());
        return ResponseEntity.ok().build();
    }
}
