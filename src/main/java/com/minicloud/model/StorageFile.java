package com.minicloud.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "storage_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "bucket_id")
    private Bucket bucket;

    private LocalDateTime uploadedAt;
}
