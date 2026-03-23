package com.minicloud.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "load_balancers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoadBalancer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private int publicPort;
    private String algorithm; // e.g., ROUND_ROBIN
    private String status; // ACTIVE, INACTIVE
    private String owner;

    @ManyToMany
    @JoinTable(
        name = "load_balancer_instances",
        joinColumns = @JoinColumn(name = "load_balancer_id"),
        inverseJoinColumns = @JoinColumn(name = "compute_instance_id")
    )
    private List<ComputeInstance> targetInstances;
}
