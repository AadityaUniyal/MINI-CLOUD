package com.minicloud.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "buckets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bucket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String owner;

    private String region;
    private String accessControl;
    private String arn;
    private boolean versioningEnabled;
    private boolean websiteEnabled;
    private String indexDocument; // e.g., index.html
    private String errorDocument; // e.g., error.html
    private LocalDateTime createdAt;
}
