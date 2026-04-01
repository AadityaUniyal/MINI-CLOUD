package com.minicloud.controller;

import com.minicloud.model.LambdaFunction;
import com.minicloud.service.compute.LambdaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lambda")
public class LambdaController {

    private final LambdaService lambdaService;

    public LambdaController(LambdaService lambdaService) {
        this.lambdaService = lambdaService;
    }

    @PostMapping("/function")
    public ResponseEntity<LambdaFunction> createFunction(@RequestBody Map<String, String> request, Principal principal) {
        return ResponseEntity.ok(lambdaService.createFunction(
                principal.getName(),
                request.get("name"),
                request.get("runtime"),
                request.get("handler"),
                request.get("code")
        ));
    }

    @GetMapping("/functions")
    public List<LambdaFunction> listFunctions(Principal principal) {
        return lambdaService.getFunctionsByOwner(principal.getName());
    }

    @PostMapping("/invoke/{name}")
    public ResponseEntity<String> invokeFunction(@PathVariable String name, @RequestBody(required = false) String payload) {
        return ResponseEntity.ok(lambdaService.invokeFunction(name, payload));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteFunction(@PathVariable String name) {
        lambdaService.deleteFunction(name);
        return ResponseEntity.ok().build();
    }
}
