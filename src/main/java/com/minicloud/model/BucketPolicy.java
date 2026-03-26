package com.minicloud.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bucket_policies")
public class BucketPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String bucketName;

    @Column(columnDefinition = "TEXT")
    private String policyJson;

    public BucketPolicy() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBucketName() { return bucketName; }
    public void setBucketName(String bucketName) { this.bucketName = bucketName; }
    public String getPolicyJson() { return policyJson; }
    public void setPolicyJson(String policyJson) { this.policyJson = policyJson; }
}
