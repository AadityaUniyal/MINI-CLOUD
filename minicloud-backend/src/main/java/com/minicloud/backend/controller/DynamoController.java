package com.minicloud.backend.controller;

import com.minicloud.backend.model.DynamoTable;
import com.minicloud.backend.service.storage.DynamoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dynamodb")
public class DynamoController {

    private final DynamoService dynamoService;

    public DynamoController(DynamoService dynamoService) {
        this.dynamoService = dynamoService;
    }

    @PostMapping("/table")
    public ResponseEntity<DynamoTable> createTable(@RequestBody Map<String, String> request, Principal principal) {
        return ResponseEntity.ok(dynamoService.createTable(
                principal.getName(),
                request.get("tableName"),
                request.get("partitionKey"),
                request.get("sortKey")
        ));
    }

    @GetMapping("/tables")
    public List<DynamoTable> listTables(Principal principal) {
        return dynamoService.getTablesByOwner(principal.getName());
    }

    @PostMapping("/table/{tableName}/item")
    public ResponseEntity<Void> putItem(@PathVariable String tableName, @RequestBody Map<String, String> item, Principal principal) {
        dynamoService.putItem(principal.getName(), tableName, item.get("key"), item.get("value"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/table/{tableName}/item/{key}")
    public ResponseEntity<String> getItem(@PathVariable String tableName, @PathVariable String key, Principal principal) {
        return dynamoService.getItem(principal.getName(), tableName, key)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
