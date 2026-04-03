package com.minicloud.compute.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "instances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Instance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instanceId; // AWS-like i-xxxxxx
    private String tenantId;
    private String name;
    private String type; // t2.micro, etc.
    private String state; // RUNNING, STOPPED, TERMINATED
    private String ipAddress;
    private String containerId; // Docker reference
    private String imageId;
    
    private LocalDateTime createdAt;
}
