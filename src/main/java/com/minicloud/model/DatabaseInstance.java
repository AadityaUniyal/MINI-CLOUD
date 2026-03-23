package com.minicloud.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "database_instances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatabaseInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // Label for the instance

    private String containerId;
    private String dbName; // Name of the actual DB inside
    private String rootPassword;
    private int hostPort;
    private String status; // running, stopped
    private String owner; // username

    private LocalDateTime createdAt;
}
