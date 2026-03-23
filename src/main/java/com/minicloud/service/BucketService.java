package com.minicloud.service;

import com.minicloud.model.Bucket;
import com.minicloud.model.StorageFile;
import com.minicloud.repository.BucketRepository;
import com.minicloud.repository.StorageFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BucketService {

    @Value("${minicloud.storage.root:./storage}")
    private String storageRoot;

    private final BucketRepository bucketRepository;
    private final StorageFileRepository storageFileRepository;

    public BucketService(BucketRepository bucketRepository, StorageFileRepository storageFileRepository) {
        this.bucketRepository = bucketRepository;
        this.storageFileRepository = storageFileRepository;
    }

    public Bucket createBucket(String name, String owner) throws IOException {
        Path bucketPath = Paths.get(storageRoot, name);
        if (!Files.exists(bucketPath)) {
            Files.createDirectories(bucketPath);
        }

        Bucket bucket = Bucket.builder()
                .name(name)
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .build();
        return bucketRepository.save(bucket);
    }

    /**
     * Week 2: Multi-Part upload handling
     */
    public StorageFile uploadFile(String bucketName, MultipartFile file, String owner) throws IOException {
        return saveFile(owner, bucketName, file.getOriginalFilename(), file.getSize(), file.getContentType(), file.getInputStream());
    }

    /**
     * Week 2 Core: Java NIO based file saving
     */
    public StorageFile saveFile(String userId, String bucketName, String fileName, long size, String contentType, java.io.InputStream data) throws IOException {
        Bucket bucket = bucketRepository.findByName(bucketName)
                .orElseThrow(() -> new RuntimeException("Bucket not found"));

        // Path: ./storage/user_id/bucket_name/file
        Path bucketPath = Paths.get(storageRoot, userId, bucketName);
        if (!Files.exists(bucketPath)) {
            Files.createDirectories(bucketPath);
        }

        Path filePath = bucketPath.resolve(fileName);
        Files.copy(data, filePath, StandardCopyOption.REPLACE_EXISTING);

        StorageFile storageFile = StorageFile.builder()
                .fileName(fileName)
                .size(size)
                .contentType(contentType)
                .bucket(bucket)
                .uploadedAt(LocalDateTime.now())
                .build();
        return storageFileRepository.save(storageFile);
    }

    public List<Bucket> getAllBuckets() {
        return bucketRepository.findAll();
    }

    /**
     * Week 2: List files in a bucket using NIO
     */
    public List<StorageFile> getFilesByBucket(String bucketName) {
        Bucket bucket = bucketRepository.findByName(bucketName)
                .orElseThrow(() -> new RuntimeException("Bucket not found"));
        return storageFileRepository.findByBucket(bucket);
    }

    /**
     * Week 2: Get file Path for download
     */
    public Path getFilePath(String userId, String bucketName, String fileName) {
        return Paths.get(storageRoot, userId, bucketName, fileName);
    }
}
