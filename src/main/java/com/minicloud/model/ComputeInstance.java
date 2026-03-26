package com.minicloud.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "compute_instances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComputeInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String containerId;
    private String image; // e.g., tomcat:latest
    private String status; // RUNNING, STOPPED, TERMINATED
    private String owner; // Username of the owner
    
    // AWS-like Metadata
    private String instanceType; // e.g., t2.micro, t2.small
    private String region; // e.g., us-east-1, ap-south-1
    private String availabilityZone; // e.g., us-east-1a
    private String publicIp; // Simulated public IP
    private String privateIp; // Simulated private IP (172.31.x.x)
    private String vpcId; // simulated vpc-xxxxx
    
    @ElementCollection
    private List<String> securityGroups;
    private String keyPairName;
    
    private Integer hostPort;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
