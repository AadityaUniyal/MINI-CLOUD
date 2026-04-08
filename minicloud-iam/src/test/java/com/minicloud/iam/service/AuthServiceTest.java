package com.minicloud.iam.service;

import com.minicloud.common.dto.AuthRequest;
import com.minicloud.common.dto.AuthResponse;
import com.minicloud.iam.model.Role;
import com.minicloud.iam.model.User;
import com.minicloud.iam.repository.RoleRepository;
import com.minicloud.iam.repository.UserRepository;
import com.minicloud.common.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        authRequest = new AuthRequest();
        authRequest.setUsername("testuser");
        authRequest.setPassword("password");
        authRequest.setEmail("test@example.com");
    }

    @Test
    void testRegister() {
        Role role = Role.builder().name("ROLE_USER").build();
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        
        User savedUser = User.builder()
                .username("testuser")
                .balance(100.0)
                .build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        AuthResponse response = authService.register(authRequest);

        assertNotNull(response);
        assertEquals("User registered successfully", response.getMessage());
        assertEquals(100.0, response.getBalance());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testLogin() {
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(mockUserDetails);
        when(jwtUtil.generateToken(mockUserDetails)).thenReturn("mockToken");
        
        User mockUser = User.builder()
                .username("testuser")
                .accountId("ACC123")
                .balance(50.0)
                .build();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        AuthResponse response = authService.login(authRequest);

        assertNotNull(response);
        assertEquals("mockToken", response.getToken());
        assertEquals("Login successful", response.getMessage());
        assertEquals("ACC123", response.getAccountId());
    }
}
