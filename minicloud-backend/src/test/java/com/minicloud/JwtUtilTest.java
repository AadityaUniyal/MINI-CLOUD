package com.minicloud;

import com.minicloud.common.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        userDetails = new User("testuser", "password", new ArrayList<>());
    }

    @Test
    void testGenerateAndValidateToken() {
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token, userDetails));
        assertEquals("testuser", jwtUtil.extractUsername(token));
    }
}
