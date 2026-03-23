package com.minicloud.controller;

import com.minicloud.model.StorageFile;
import com.minicloud.service.BucketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/storage")
public class StorageController {

    private final BucketService bucketService;

    public StorageController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @PostMapping("/upload")
    public ResponseEntity<StorageFile> uploadFile(
            @RequestParam String bucketName,
            @RequestParam MultipartFile file) throws IOException {
        StorageFile storageFile = bucketService.uploadFile(bucketName, file, "admin");
        return ResponseEntity.ok(storageFile);
    }

    @GetMapping("/files")
    public ResponseEntity<List<StorageFile>> getFilesByBucket(@RequestParam String bucketName) {
        return ResponseEntity.ok(bucketService.getFilesByBucket(bucketName));
    }
}
