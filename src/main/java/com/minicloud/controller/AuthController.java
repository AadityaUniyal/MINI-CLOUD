package com.minicloud.controller;

import com.minicloud.dto.*;
import com.minicloud.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register-aws")
    public ResponseEntity<AuthResponse> registerAws(@RequestBody AwsRegisterRequest request) {
        return ResponseEntity.ok(authService.registerAws(request));
    }

    @PostMapping("/login-aws")
    public ResponseEntity<AuthResponse> loginAws(@RequestBody AwsLoginRequest request) {
        return ResponseEntity.ok(authService.loginAws(request));
    }

    @PostMapping("/iam/create")
    public ResponseEntity<com.minicloud.model.User> createIamUser(@RequestParam String iamUsername, 
                                                                 @RequestParam String password, 
                                                                 Principal principal) {
        return ResponseEntity.ok(authService.createIamUser(principal.getName(), iamUsername, password));
    }

    @GetMapping("/iam/list")
    public ResponseEntity<List<com.minicloud.model.User>> listIamUsers(Principal principal) {
        return ResponseEntity.ok(authService.getIamUsersForRoot(principal.getName()));
    }

    @GetMapping("/me")
    public ResponseEntity<com.minicloud.model.User> getMe(Principal principal) {
        return ResponseEntity.ok(authService.getUserFromToken(principal.getName()));
    }
}
