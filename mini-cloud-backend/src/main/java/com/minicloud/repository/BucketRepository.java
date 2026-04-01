package com.minicloud.repository;

import com.minicloud.model.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface BucketRepository extends JpaRepository<Bucket, Long> {
    Optional<Bucket> findByName(String name);
    List<Bucket> findByOwner(String owner);
}
