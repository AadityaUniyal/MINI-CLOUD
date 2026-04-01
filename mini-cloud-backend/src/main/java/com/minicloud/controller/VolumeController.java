package com.minicloud.controller;

import com.minicloud.dto.CreateVolumeRequest;
import com.minicloud.model.Volume;
import com.minicloud.service.storage.VolumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ebs")
public class VolumeController {

    private final VolumeService volumeService;

    public VolumeController(VolumeService volumeService) {
        this.volumeService = volumeService;
    }

    @PostMapping("/create")
    public ResponseEntity<Volume> createVolume(@RequestBody CreateVolumeRequest request, Principal principal) {
        return ResponseEntity.ok(volumeService.createVolume(principal.getName(), request));
    }

    @DeleteMapping("/delete/{volumeId}")
    public ResponseEntity<Void> deleteVolume(@PathVariable String volumeId, Principal principal) {
        volumeService.deleteVolume(principal.getName(), volumeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/attach")
    public ResponseEntity<Volume> attachVolume(@RequestBody Map<String, String> payload, Principal principal) {
        String volumeId = payload.get("volumeId");
        String instanceId = payload.get("instanceId");
        String deviceName = payload.get("deviceName");
        return ResponseEntity.ok(volumeService.attachVolume(principal.getName(), volumeId, instanceId, deviceName));
    }

    @PostMapping("/detach/{volumeId}")
    public ResponseEntity<Volume> detachVolume(@PathVariable String volumeId, Principal principal) {
        return ResponseEntity.ok(volumeService.detachVolume(principal.getName(), volumeId));
    }

    @GetMapping("/volumes")
    public ResponseEntity<List<Volume>> listVolumes(Principal principal) {
        return ResponseEntity.ok(volumeService.listVolumes(principal.getName()));
    }
}
