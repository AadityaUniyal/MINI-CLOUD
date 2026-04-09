package com.minicloud.iam.controller;

import com.minicloud.common.dto.*;
import com.minicloud.iam.model.User;
import com.minicloud.iam.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Fix #15: All endpoints that previously returned the raw User entity
 * now return UserResponseDto which excludes the hashed password field.
 */
@RestController
@RequestMapping("/api/v1/auth")
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

    @PostMapping("/register-MiniCloud")
    public ResponseEntity<AuthResponse> registerMiniCloud(@RequestBody AwsRegisterRequest request) {
        return ResponseEntity.ok(authService.registerAws(request));
    }

    @PostMapping("/login-MiniCloud")
    public ResponseEntity<AuthResponse> loginMiniCloud(@RequestBody AwsLoginRequest request) {
        return ResponseEntity.ok(authService.loginAws(request));
    }

    /** Fix #15: returns UserResponseDto, not the raw User entity (which contained the password). */
    @PostMapping("/iam/create")
    public ResponseEntity<UserResponseDto> createIamUser(@RequestParam String iamUsername,
                                                         @RequestParam String password,
                                                         Principal principal) {
        User user = authService.createIamUser(principal.getName(), iamUsername, password);
        return ResponseEntity.ok(toDto(user));
    }

    /** Fix #15: returns list of UserResponseDto without passwords. */
    @GetMapping("/iam/list")
    public ResponseEntity<List<UserResponseDto>> listIamUsers(Principal principal) {
        List<UserResponseDto> users = authService.getIamUsersForRoot(principal.getName())
                .stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    /** Fix #15: returns UserResponseDto without password. */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe(Principal principal) {
        User user = authService.getUserByUsername(principal.getName());
        return ResponseEntity.ok(toDto(user));
    }

    private UserResponseDto toDto(User u) {
        return UserResponseDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .accountId(u.getAccountId())
                .accountAlias(u.getAccountAlias())
                .balance(u.getBalance())
                .status(u.getStatus())
                .rootUser(u.isRootUser())
                .iamUsername(u.getIamUsername())
                .accessKey(u.getAccessKey())   // only set on create; null for subsequent reads
                .roles(u.getRoles() == null ? null :
                        u.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet()))
                .createdAt(u.getCreatedAt())
                .build();
    }
}
