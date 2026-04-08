package com.minicloud.iam.service;

import com.minicloud.common.dto.*;
import com.minicloud.iam.model.Role;
import com.minicloud.iam.model.User;
import com.minicloud.iam.repository.RoleRepository;
import com.minicloud.iam.repository.UserRepository;
import com.minicloud.common.security.JwtUtil;
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
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
                       JwtUtil jwtUtil, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(AuthRequest request) {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));
                
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail() != null ? request.getEmail() : request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(java.util.Set.of(userRole))
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
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));
                
        User user = User.builder()
                .username(request.getEmail())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(java.util.Set.of(adminRole))
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

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));

        String accessKey = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase();
        String secretKey = java.util.UUID.randomUUID().toString().replace("-", "");

        User iamUser = User.builder()
                .username(root.getAccountId() + "/" + iamUsername)
                .email(iamUsername + "@" + root.getAccountId() + ".minicloud.internal")
                .iamUsername(iamUsername)
                .accountId(root.getAccountId())
                .isRootUser(false)
                .password(passwordEncoder.encode(password))
                .roles(java.util.Set.of(userRole))
                .owner(rootUsername)
                .balance(0.0)
                .status("ACTIVE")
                .build();

        User saved = userRepository.save(iamUser);
        // Attach generated keys to the returned object (visible only once to caller)
        saved.setAccessKey(accessKey);
        saved.setSecretKey(secretKey);
        return saved;
    }

    public java.util.List<User> getIamUsersForRoot(String rootUsername) {
        return userRepository.findAll().stream()
                .filter(u -> rootUsername.equals(u.getOwner()) && !u.isRootUser())
                .toList();
    }

    private String generateAccountId() {
        // UUID-based: eliminates Math.random() collision risk
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    /** Returns the user by username (principal.getName() — NOT a JWT string). */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
