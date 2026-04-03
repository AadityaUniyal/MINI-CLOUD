package com.minicloud.backend.service.networking;

import com.minicloud.backend.model.Subnet;
import com.minicloud.backend.model.VPC;
import com.minicloud.backend.repository.SubnetRepository;
import com.minicloud.backend.repository.VPCRepository;
import com.minicloud.util.CIDRCalculator;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SubnetService {

    private final SubnetRepository subnetRepository;
    private final VPCRepository vpcRepository;

    public SubnetService(SubnetRepository subnetRepository, VPCRepository vpcRepository) {
        this.subnetRepository = subnetRepository;
        this.vpcRepository = vpcRepository;
    }

    public Subnet createSubnet(String owner, String vpcId, String cidrBlock, String az, boolean mapPublicIp) {
        VPC vpc = vpcRepository.findByVpcId(vpcId)
                .orElseThrow(() -> new IllegalArgumentException("VPC not found: " + vpcId));
        
        if (!vpc.getOwner().equals(owner)) {
            throw new SecurityException("Unauthorized access to VPC: " + vpcId);
        }

        if (!CIDRCalculator.isSubsetOf(cidrBlock, vpc.getCidrBlock())) {
            throw new IllegalArgumentException("Subnet CIDR " + cidrBlock + " is not within VPC CIDR " + vpc.getCidrBlock());
        }

        // Simple overlap check (can be improved)
        List<Subnet> existingSubnets = subnetRepository.findByVpcId(vpcId);
        for (Subnet s : existingSubnets) {
            if (s.getCidrBlock().equals(cidrBlock)) {
                throw new IllegalArgumentException("Subnet CIDR block already exists in this VPC");
            }
        }

        String subnetId = "subnet-" + UUID.randomUUID().toString().substring(0, 8);

        Subnet subnet = Subnet.builder()
                .subnetId(subnetId)
                .vpcId(vpcId)
                .cidrBlock(cidrBlock)
                .availabilityZone(az)
                .state("available")
                .owner(owner)
                .mapPublicIpOnLaunch(mapPublicIp)
                .availableIpAddressCount(251) // Standard AWS-like reservation
                .creationTime(LocalDateTime.now())
                .build();

        return subnetRepository.save(subnet);
    }

    public List<Subnet> listSubnets(String owner) {
        return subnetRepository.findByOwner(owner);
    }

    public List<Subnet> getSubnetsByVpc(String vpcId) {
        return subnetRepository.findByVpcId(vpcId);
    }

    public void deleteSubnet(String subnetId, String owner) {
        subnetRepository.findBySubnetId(subnetId).ifPresent(s -> {
            if (s.getOwner().equals(owner)) {
                subnetRepository.delete(s);
            } else {
                throw new SecurityException("Unauthorized to delete this subnet");
            }
        });
    }
}
