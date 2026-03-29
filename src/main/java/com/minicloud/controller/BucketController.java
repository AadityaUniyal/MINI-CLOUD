package com.minicloud.controller;

import com.minicloud.model.Bucket;
import com.minicloud.model.StorageFile;
import com.minicloud.service.BucketService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/buckets")
public class BucketController {

    private final BucketService bucketService;

    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @PostMapping
    public ResponseEntity<Bucket> createBucket(@RequestParam String name, 
                                               @RequestParam(required = false) String region,
                                               Principal principal) throws IOException {
        return ResponseEntity.ok(bucketService.createBucket(name, principal.getName(), region));
    }

    @GetMapping
    public List<Bucket> listBuckets(Authentication auth) {
        return bucketService.getBucketsByOwner(auth.getName());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteBucket(@PathVariable String name, Authentication auth) {
        bucketService.deleteBucket(name, auth.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{bucketName}/upload")
    public ResponseEntity<StorageFile> uploadFile(@PathVariable String bucketName, 
                                                   @RequestParam("file") MultipartFile file, 
                                                   Principal principal) throws IOException {
        return ResponseEntity.ok(bucketService.uploadFile(bucketName, file, principal.getName()));
    }

    @GetMapping("/{bucketName}/files")
    public ResponseEntity<List<StorageFile>> listFiles(@PathVariable String bucketName, Principal principal) {
        return ResponseEntity.ok(bucketService.getFilesByBucket(bucketName, principal.getName()));
    }

    @GetMapping("/{bucketName}/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String bucketName, 
                                                 @PathVariable String fileName, 
                                                 Principal principal) throws IOException {
        Path filePath = bucketService.getFilePath(principal.getName(), bucketName, fileName);
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/{name}/website")
    public ResponseEntity<Bucket> enableWebsite(@PathVariable String name, 
                                                @RequestParam String indexDocument,
                                                @RequestParam(required = false) String errorDocument,
                                                Principal principal) {
        return ResponseEntity.ok(bucketService.enableWebsiteHosting(name, principal.getName(), indexDocument, errorDocument));
    }

    @GetMapping("/public/{bucketName}/{*path}")
    public ResponseEntity<Resource> proxyPublicFile(@PathVariable String bucketName, 
                                                     @PathVariable(required = false) String path) throws IOException {
        Path filePath = bucketService.resolveWebsiteFile(bucketName, path);
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }
        
        Resource resource = new UrlResource(filePath.toUri());
        String contentType = bucketService.getContentType(filePath);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
