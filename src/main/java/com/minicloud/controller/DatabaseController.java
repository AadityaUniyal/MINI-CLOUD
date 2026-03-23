package com.minicloud.controller;

import com.minicloud.dto.ProvisionDbRequest;
import com.minicloud.model.DatabaseInstance;
import com.minicloud.service.DatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/provision")
    public ResponseEntity<DatabaseInstance> provisionDatabase(@RequestBody ProvisionDbRequest request) {
        // In a real app, user details would be taken from the SecurityContext
        DatabaseInstance instance = databaseService.provisionDatabase("admin", 
            request.getName(), request.getDbName(), request.getRootPassword());
        return ResponseEntity.ok(instance);
    }

    @GetMapping("/instances")
    public ResponseEntity<List<DatabaseInstance>> getAllDatabases() {
        return ResponseEntity.ok(databaseService.getAllDatabases());
    }

    @PostMapping("/stop/{name}")
    public ResponseEntity<Void> stopDatabase(@PathVariable String name) {
        databaseService.stopDatabase(name);
        return ResponseEntity.ok().build();
    }
}
