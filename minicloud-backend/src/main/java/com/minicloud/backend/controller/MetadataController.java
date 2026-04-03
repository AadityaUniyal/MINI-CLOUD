package com.minicloud.backend.controller;

import com.minicloud.backend.service.CloudMetadataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/metadata")
public class MetadataController {

    private final CloudMetadataService metadataService;

    public MetadataController(CloudMetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @GetMapping("/options")
    public ResponseEntity<Map<String, List<String>>> getCloudOptions() {
        return ResponseEntity.ok(metadataService.getCloudOptions());
    }
}
