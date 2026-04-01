package com.minicloud.repository;

import com.minicloud.model.Volume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VolumeRepository extends JpaRepository<Volume, Long> {
    Optional<Volume> findByVolumeId(String volumeId);
    List<Volume> findByOwner(String owner);
    List<Volume> findByAttachedInstanceId(String attachedInstanceId);
}
