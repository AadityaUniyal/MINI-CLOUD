package com.minicloud.compute.repository;

import com.minicloud.compute.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByImageId(String imageId);
}
