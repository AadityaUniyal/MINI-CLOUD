package com.minicloud.compute.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String imageId; // ami-xxxxxx
    
    private String name;
    private String osType;
    private String version;
    private String dockerImage; // e.g. ubuntu:latest
    private boolean isPublic;
}
