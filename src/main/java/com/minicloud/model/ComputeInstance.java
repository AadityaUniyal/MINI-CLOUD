package com.minicloud.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
    private String publicIp; // Simulated IP (e.g., localhost:8080)
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
