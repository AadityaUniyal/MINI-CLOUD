package com.minicloud.service;

import com.minicloud.dto.*;
import com.minicloud.model.User;
import com.minicloud.repository.UserRepository;
import com.minicloud.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
                       JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(AuthRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .build();
        User savedUser = userRepository.save(user);
        return AuthResponse.builder()
                .message("User registered successfully")
                .balance(savedUser.getBalance())
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String token = jwtUtil.generateToken(userDetails);
        
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return AuthResponse.builder()
                .token(token)
                .message("Login successful")
                .balance(user.getBalance())
                .accountId(user.getAccountId())
                .username(user.getUsername())
                .build();
    }

    public AuthResponse registerAws(AwsRegisterRequest request) {
        String accountId = generateAccountId();
        User user = User.builder()
                .username(request.getEmail())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_ADMIN")
                .isRootUser(true)
                .accountId(accountId)
                .accountAlias(request.getAccountAlias())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .country(request.getCountry())
                .status("ACTIVE")
                .build();
        
        userRepository.save(user);
        return AuthResponse.builder()
                .message("AWS Root Account created successfully. Your Account ID is: " + accountId)
                .build();
    }

    public AuthResponse loginAws(AwsLoginRequest request) {
        User user;
        if (request.isRootUser()) {
            user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Root account not found"));
        } else {
            // IAM Login: Try by ID then Alias
            user = userRepository.findByAccountIdAndIamUsername(request.getAccountId(), request.getIamUsername())
                    .or(() -> userRepository.findByAccountAliasAndIamUsername(request.getAccountId(), request.getIamUsername()))
                    .orElseThrow(() -> new RuntimeException("IAM user not found for account: " + request.getAccountId()));
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .message("AWS Login successful")
                .balance(user.getBalance())
                .accountId(user.getAccountId())
                .username(user.getUsername())
                .build();
    }

    public User createIamUser(String rootUsername, String iamUsername, String password) {
        User root = userRepository.findByUsername(rootUsername)
                .orElseThrow(() -> new RuntimeException("Root user not found"));
        
        if (!root.isRootUser()) {
            throw new RuntimeException("Only root users can create IAM users");
        }

        User iamUser = User.builder()
                .username(root.getAccountId() + "/" + iamUsername)
                .iamUsername(iamUsername)
                .accountId(root.getAccountId())
                .isRootUser(false)
                .password(passwordEncoder.encode(password))
                .role("ROLE_USER")
                .owner(rootUsername)
                .balance(0.0) // IAM users share root resources or have 0 starting balance
                .status("ACTIVE")
                .build();
        
        return userRepository.save(iamUser);
    }

    public java.util.List<User> getIamUsersForRoot(String rootUsername) {
        return userRepository.findAll().stream()
                .filter(u -> rootUsername.equals(u.getOwner()) && !u.isRootUser())
                .toList();
    }

    private String generateAccountId() {
        return String.format("%012d", (long) (Math.random() * 1000000000000L));
    }

    public User getUserFromToken(String token) {
        String username = jwtUtil.extractUsername(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
