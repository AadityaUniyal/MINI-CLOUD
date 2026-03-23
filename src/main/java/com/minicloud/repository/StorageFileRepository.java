package com.minicloud.repository;

import com.minicloud.model.Bucket;
import com.minicloud.model.StorageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StorageFileRepository extends JpaRepository<StorageFile, Long> {
    List<StorageFile> findByBucket(Bucket bucket);
}
