package com.minicloud.backend.repository;

import com.minicloud.backend.model.BucketVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BucketVersionRepository extends JpaRepository<BucketVersion, Long> {
    List<BucketVersion> findByBucketNameAndFileName(String bucketName, String fileName);
    Optional<BucketVersion> findByBucketNameAndFileNameAndIsLatestTrue(String bucketName, String fileName);
    List<BucketVersion> findByBucketName(String bucketName);
}
