package com.minicloud.billing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "usage_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenantId;
    private String resourceType; // COMPUTE, STORAGE, DATABASE
    private String resourceId;
    private Double quantity;
    private LocalDateTime timestamp;
}
