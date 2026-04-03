package com.minicloud.backend.repository;

import com.minicloud.backend.model.Bucket;
import com.minicloud.backend.model.StorageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageFileRepository extends JpaRepository<StorageFile, Long> {
    List<StorageFile> findByBucket(Bucket bucket);
}
