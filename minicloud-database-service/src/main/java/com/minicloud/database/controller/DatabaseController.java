package com.minicloud.database.controller;

import com.minicloud.database.service.DatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/databases")
public class DatabaseController {

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> provisionDatabase(@RequestParam String engine,
                                                                @RequestParam String dbName,
                                                                @RequestParam String user,
                                                                @RequestParam String password) {
        String containerId = databaseService.provisionDatabase(engine, dbName, user, password);
        String ip = databaseService.getDatabaseIp(containerId);
        
        return ResponseEntity.ok(Map.of(
            "containerId", containerId,
            "ip", ip,
            "status", "PROVISIONED"
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> terminateDatabase(@PathVariable String id) {
        databaseService.terminateDatabase(id);
        return ResponseEntity.ok().build();
    }
}
