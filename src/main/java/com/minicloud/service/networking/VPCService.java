package com.minicloud.service.networking;

import com.minicloud.model.VPC;
import com.minicloud.repository.VPCRepository;
import com.minicloud.util.CIDRCalculator;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VPCService {

    private final VPCRepository vpcRepository;

    public VPCService(VPCRepository vpcRepository) {
        this.vpcRepository = vpcRepository;
    }

    public VPC createVPC(String owner, String cidrBlock, boolean enableDnsSupport, boolean enableDnsHostnames) {
        if (!CIDRCalculator.isValidCIDR(cidrBlock)) {
            throw new IllegalArgumentException("Invalid CIDR block: " + cidrBlock);
        }

        String vpcId = "vpc-" + UUID.randomUUID().toString().substring(0, 8);

        VPC vpc = VPC.builder()
                .vpcId(vpcId)
                .cidrBlock(cidrBlock)
                .state("available")
                .tenancy("default")
                .isDefault(false)
                .enableDnsSupport(enableDnsSupport)
                .enableDnsHostnames(enableDnsHostnames)
                .owner(owner)
                .creationTime(LocalDateTime.now())
                .build();

        return vpcRepository.save(vpc);
    }

    public void deleteVPC(String vpcId, String owner) {
        vpcRepository.findByVpcId(vpcId).ifPresent(vpc -> {
            if (vpc.getOwner().equals(owner)) {
                vpcRepository.delete(vpc);
            } else {
                throw new SecurityException("Unauthorized to delete this VPC");
            }
        });
    }

    public List<VPC> describeVPCs(String owner) {
        return vpcRepository.findByOwner(owner);
    }
}
