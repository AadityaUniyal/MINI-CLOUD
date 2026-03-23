package com.minicloud.repository;

import com.minicloud.model.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
    Optional<Bucket> findByName(String name);
}
