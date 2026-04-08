package com.minicloud.common.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testSecret = "minicloud_test_secret_key_64_characters_long_for_security_testing_purpose";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secretKey", testSecret);
    }

    @Test
    void testGenerateAndValidateToken() {
        UserDetails userDetails = new User("testuser", "password", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);

        assertTrue(jwtUtil.validateToken(token, userDetails));
        assertEquals("testuser", jwtUtil.extractUsername(token));
    }

    @Test
    void testExtractRoles() {
        UserDetails userDetails = new User("admin", "password", 
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER")));

        String token = jwtUtil.generateToken(userDetails);
        List<String> roles = jwtUtil.extractRoles(token);

        assertNotNull(roles);
        assertTrue(roles.contains("ROLE_ADMIN"));
        assertTrue(roles.contains("ROLE_USER"));
    }

    @Test
    void testValidateTokenWithWrongUser() {
        UserDetails user1 = new User("user1", "pass", Collections.emptyList());
        UserDetails user2 = new User("user2", "pass", Collections.emptyList());

        String token = jwtUtil.generateToken(user1);
        assertFalse(jwtUtil.validateToken(token, user2));
    }
}
