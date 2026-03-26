package com.minicloud.controller;

import com.minicloud.dto.ProvisionDbRequest;
import com.minicloud.model.DatabaseInstance;
import com.minicloud.service.DatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/provision")
    public ResponseEntity<DatabaseInstance> provisionDatabase(@RequestBody ProvisionDbRequest request, Principal principal) {
        return ResponseEntity.ok(databaseService.provisionDatabase(principal.getName(), request));
    }

    @PostMapping("/replica/{sourceId}")
    public ResponseEntity<DatabaseInstance> createReadReplica(@PathVariable Long sourceId, Principal principal) {
        return ResponseEntity.ok(databaseService.createReadReplica(principal.getName(), sourceId));
    }

    @GetMapping("/instances")
    public List<DatabaseInstance> listDatabases(Authentication auth) {
        return databaseService.getDatabasesByOwner(auth.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDatabase(@PathVariable Long id, Authentication auth) {
        databaseService.terminateDatabase(id, auth.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stop/{name}")
    public ResponseEntity<Void> stopDatabase(@PathVariable String name) {
        databaseService.stopDatabase(name);
        return ResponseEntity.ok().build();
    }
}
