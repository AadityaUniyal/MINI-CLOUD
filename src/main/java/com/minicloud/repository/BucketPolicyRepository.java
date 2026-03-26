package com.minicloud.repository;

import com.minicloud.model.BucketPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BucketPolicyRepository extends JpaRepository<BucketPolicy, Long> {
    Optional<BucketPolicy> findByBucketName(String bucketName);
}
