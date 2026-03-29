package com.minicloud.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "database_instances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String containerId;
    private String dbName;
    private String rootPassword;
    private int hostPort;
    private String status; // running, stopped
    private String owner; // username

    // RDS-like Metadata
    private String engine; // mysql, postgres
    private String engineVersion; // 8.0
    private String dbInstanceClass; // db.t3.micro
    private String readReplicaSourceId;
    private String vpcId;
    private String subnetId;
    private String availabilityZone;
    private String storageType; // gp2
    private Integer allocatedStorage; // GB
    private boolean multiAz;
    private boolean publiclyAccessible;
    private Integer backupRetention; // days
    private LocalDateTime createdAt;
}
