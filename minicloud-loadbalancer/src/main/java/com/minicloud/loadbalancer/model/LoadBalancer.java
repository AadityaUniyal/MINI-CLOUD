package com.minicloud.loadbalancer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "load_balancers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadBalancer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type; // APPLICATION, NETWORK
    private String ipAddress;
    private String status;
    private String tenantId;
}
