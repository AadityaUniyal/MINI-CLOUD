package com.minicloud.backend.service;

import com.minicloud.backend.model.Bucket;
import com.minicloud.backend.model.StorageFile;
import com.minicloud.backend.model.AuditLog;
import com.minicloud.backend.repository.AuditLogRepository;
import com.minicloud.backend.repository.BucketRepository;
import com.minicloud.backend.repository.StorageFileRepository;
import com.minicloud.backend.repository.BucketVersionRepository;
import com.minicloud.backend.repository.BucketPolicyRepository;
import com.minicloud.backend.model.BucketVersion;
import com.minicloud.backend.model.BucketPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BucketService {

    @Value("${minicloud.storage.root:./storage}")
    private String storageRoot;

    private final BucketRepository bucketRepository;
    private final StorageFileRepository storageFileRepository;
    private final AuditLogRepository auditLogRepository;
    private final BucketVersionRepository bucketVersionRepository;
    private final BucketPolicyRepository bucketPolicyRepository;

    public BucketService(BucketRepository bucketRepository, 
                         StorageFileRepository storageFileRepository, 
                         AuditLogRepository auditLogRepository,
                         BucketVersionRepository bucketVersionRepository,
                         BucketPolicyRepository bucketPolicyRepository) {
        this.bucketRepository = bucketRepository;
        this.storageFileRepository = storageFileRepository;
        this.auditLogRepository = auditLogRepository;
        this.bucketVersionRepository = bucketVersionRepository;
        this.bucketPolicyRepository = bucketPolicyRepository;
    }

    public Bucket createBucket(String name, String owner, String region) throws IOException {
        Path bucketPath = Paths.get(storageRoot, name);
        if (!Files.exists(bucketPath)) {
            Files.createDirectories(bucketPath);
        }

        Bucket bucket = Bucket.builder()
                .name(name)
                .owner(owner)
                .region(region != null ? region : "us-east-1")
                .accessControl("Private")
                .arn("arn:aws:s3:::" + name)
                .createdAt(LocalDateTime.now())
                .build();
        
        auditLogRepository.save(AuditLog.builder()
            .username(owner)
            .action("CREATE_BUCKET")
            .resourceId(name)
            .details("Created storage bucket: " + name + " in region: " + (region != null ? region : "us-east-1"))
            .timestamp(LocalDateTime.now())
            .build());

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
        
        if (bucket.isVersioningEnabled()) {
            handleVersioning(userId, bucketName, fileName, size, contentType, data);
        } else {
            Files.copy(data, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        StorageFile storageFile = StorageFile.builder()
                .fileName(fileName)
                .size(size)
                .contentType(contentType)
                .bucket(bucket)
                .uploadedAt(LocalDateTime.now())
                .build();

        auditLogRepository.save(AuditLog.builder()
                .username(userId)
                .action("UPLOAD_FILE")
                .resourceId(fileName)
                .details("Uploaded " + fileName + " to bucket " + bucketName)
                .timestamp(LocalDateTime.now())
                .build());

        return storageFileRepository.save(storageFile);
    }

    private void handleVersioning(String userId, String bucketName, String fileName, long size, String contentType, java.io.InputStream data) throws IOException {
        String versionId = UUID.randomUUID().toString().substring(0, 8);
        Path bucketPath = Paths.get(storageRoot, userId, bucketName);
        Path versionPath = bucketPath.resolve(fileName + "." + versionId);
        
        Files.copy(data, versionPath, StandardCopyOption.REPLACE_EXISTING);
        
        // Mark old versions as not latest
        bucketVersionRepository.findByBucketNameAndFileNameAndIsLatestTrue(bucketName, fileName)
            .ifPresent(old -> {
                old.setLatest(false);
                bucketVersionRepository.save(old);
            });
            
        BucketVersion version = BucketVersion.builder()
                .bucketName(bucketName)
                .fileName(fileName)
                .versionId(versionId)
                .filePath(versionPath.toString())
                .size(size)
                .contentType(contentType)
                .isLatest(true)
                .createdAt(LocalDateTime.now())
                .build();
        bucketVersionRepository.save(version);
        
        // Update main file pointer to the latest version
        Path mainFilePath = bucketPath.resolve(fileName);
        Files.copy(versionPath, mainFilePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public List<Bucket> getBucketsByOwner(String owner) {
        return bucketRepository.findByOwner(owner);
    }

    /**
     * Week 2: List files in a bucket using NIO
     */
    public List<StorageFile> getFilesByBucket(String bucketName, String owner) {
        Bucket bucket = bucketRepository.findByName(bucketName)
                .filter(b -> b.getOwner().equals(owner))
                .orElseThrow(() -> new RuntimeException("Bucket not found or access denied"));
        return storageFileRepository.findByBucket(bucket);
    }

    /**
     * Week 2: Get file Path for download
     */
    public void deleteBucket(String name, String owner) {
        bucketRepository.findByName(name).ifPresent(bucket -> {
            if (bucket.getOwner().equals(owner)) {
                try {
                    // Delete files from filesystem
                    Path bucketPath = Paths.get(storageRoot, owner, name);
                    if (Files.exists(bucketPath)) {
                        Files.walk(bucketPath)
                             .sorted(java.util.Comparator.reverseOrder())
                             .map(Path::toFile)
                             .forEach(java.io.File::delete);
                    }
                    
                    // Delete file records
                    storageFileRepository.deleteAll(storageFileRepository.findByBucket(bucket));
                    
                    // Delete bucket record
                    bucketRepository.delete(bucket);

                    // Audit Log
                    auditLogRepository.save(AuditLog.builder()
                        .username(owner)
                        .action("DELETE_BUCKET")
                        .resourceId(name)
                        .details("Deleted bucket and all files: " + name)
                        .timestamp(LocalDateTime.now())
                        .build());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete bucket files", e);
                }
            }
        });
    }

    public Path getFilePath(String userId, String bucketName, String fileName) {
        return Paths.get(storageRoot, userId, bucketName, fileName);
    }

    public void deleteFile(String bucketName, String fileName) {
        deleteFile(bucketName, fileName, "system");
    }

    public void deleteFile(String bucketName, String fileName, String owner) {
        try {
            Path filePath = getFilePath(owner, bucketName, fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            storageFileRepository.findByFileNameAndBucketName(fileName, bucketName)
                .ifPresent(storageFileRepository::delete);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + fileName, e);
        }
    }

    public BucketPolicy setBucketPolicy(String bucketName, String owner, String policyJson) {
        Bucket bucket = bucketRepository.findByName(bucketName)
                .filter(b -> b.getOwner().equals(owner))
                .orElseThrow(() -> new RuntimeException("Bucket not found or access denied"));
        
        BucketPolicy policy = bucketPolicyRepository.findByBucketName(bucketName)
                .orElse(new BucketPolicy());
        policy.setBucketName(bucketName);
        policy.setPolicyJson(policyJson);
        return bucketPolicyRepository.save(policy);
    }

    public Bucket enableWebsiteHosting(String name, String owner, String indexDoc, String errorDoc) {
        Bucket bucket = bucketRepository.findByName(name)
                .filter(b -> b.getOwner().equals(owner))
                .orElseThrow(() -> new RuntimeException("Bucket not found or access denied"));
        bucket.setWebsiteEnabled(true);
        bucket.setIndexDocument(indexDoc != null ? indexDoc : "index.html");
        bucket.setErrorDocument(errorDoc != null ? errorDoc : "error.html");
        return bucketRepository.save(bucket);
    }

    public Path resolveWebsiteFile(String bucketName, String path) {
        Bucket bucket = bucketRepository.findByName(bucketName)
                .filter(Bucket::isWebsiteEnabled)
                .orElseThrow(() -> new RuntimeException("Bucket website not enabled or not found"));
        
        String fileName = (path == null || path.isEmpty() || path.equals("/")) ? bucket.getIndexDocument() : path;
        return Paths.get(storageRoot, bucket.getOwner(), bucketName, fileName);
    }

    public Path resolveErrorDocument(String bucketName) {
        return bucketRepository.findByName(bucketName)
                .filter(Bucket::isWebsiteEnabled)
                .filter(b -> b.getErrorDocument() != null)
                .map(b -> Paths.get(storageRoot, b.getOwner(), bucketName, b.getErrorDocument()))
                .orElse(null);
    }

    public String getContentType(Path path) {
        try {
            String type = Files.probeContentType(path);
            if (type == null) {
                if (path.toString().endsWith(".css")) return "text/css";
                if (path.toString().endsWith(".js")) return "application/javascript";
                return "text/plain";
            }
            return type;
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    public boolean canAccess(String bucketName, String user, String action) {
        // Mock policy check: Just check ownership or public-read policy or if website is enabled
        return bucketRepository.findByName(bucketName)
                .map(b -> b.getOwner().equals(user) || "Public-Read".equals(b.getAccessControl()) || b.isWebsiteEnabled())
                .orElse(false);
    }
}
