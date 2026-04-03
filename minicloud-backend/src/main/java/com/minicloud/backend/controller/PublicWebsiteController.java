package com.minicloud.backend.controller;

import com.minicloud.backend.service.BucketService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class PublicWebsiteController {

    private final BucketService bucketService;

    public PublicWebsiteController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @GetMapping("/{bucketName}/**")
    public ResponseEntity<Resource> serveWebsite(@PathVariable String bucketName, HttpServletRequest request) throws IOException {
        String requestURI = request.getRequestURI();
        String path = requestURI.substring(bucketName.length() + 2); // Removes /{bucketName}/
        
        // Skip api routes or internal static files
        if (bucketName.equals("api") || bucketName.equals("h2-console")) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path filePath = bucketService.resolveWebsiteFile(bucketName, path.isEmpty() ? null : path);
            if (!Files.exists(filePath)) {
                // Check for error document
                Path errorPath = bucketService.resolveErrorDocument(bucketName);
                if (errorPath != null && Files.exists(errorPath)) {
                    Resource resource = new UrlResource(errorPath.toUri());
                    return ResponseEntity.status(404)
                            .contentType(MediaType.parseMediaType(bucketService.getContentType(errorPath)))
                            .body(resource);
                }
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());
            String contentType = bucketService.getContentType(filePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Bucket website not enabled or not found
        }
    }
}
