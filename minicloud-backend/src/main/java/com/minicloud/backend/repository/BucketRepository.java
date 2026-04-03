package com.minicloud.backend.repository;

import com.minicloud.backend.model.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface BucketRepository extends JpaRepository<Bucket, Long> {
    Optional<Bucket> findByName(String name);
    List<Bucket> findByOwner(String owner);
}
