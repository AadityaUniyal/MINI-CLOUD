package com.minicloud.compute.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instance_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstanceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    
    private Integer vcpu;
    private Integer memoryGb;
    private Integer diskGb;
    private Double pricePerHour;
}
