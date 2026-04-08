package com.saaskit.starter.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private static final String TEST_SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "secretKey", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "accessTokenExpiration", 86400000L);
        ReflectionTestUtils.setField(jwtUtils, "refreshTokenExpiration", 604800000L);
    }

    @Test
    void shouldGenerateAccessTokenAndExtractEmail() {
        String email = "test@example.com";

        String token = jwtUtils.generateToken(email);

        assertNotNull(token);
        assertEquals(email, jwtUtils.extractEmail(token));
    }

    @Test
    void shouldValidateAccessTokenForCorrectUser() {
        String email = "test@example.com";
        String token = jwtUtils.generateToken(email);

        assertTrue(jwtUtils.isTokenValid(token, email));
    }

    @Test
    void shouldInvalidateAccessTokenForWrongUser() {
        String email = "test@example.com";
        String token = jwtUtils.generateToken(email);

        assertFalse(jwtUtils.isTokenValid(token, "wrong@example.com"));
    }

    @Test
    void shouldGenerateRefreshTokenAndExtractEmail() {
        String email = "test@example.com";

        String refreshToken = jwtUtils.generateRefreshToken(email);

        assertNotNull(refreshToken);
        assertEquals(email, jwtUtils.extractEmail(refreshToken));
    }

    @Test
    void shouldIdentifyRefreshTokenCorrectly() {
        String email = "test@example.com";

        String accessToken = jwtUtils.generateToken(email);
        String refreshToken = jwtUtils.generateRefreshToken(email);

        assertFalse(jwtUtils.isRefreshToken(accessToken));
        assertTrue(jwtUtils.isRefreshToken(refreshToken));
    }

    @Test
    void shouldValidateRefreshToken() {
        String email = "test@example.com";
        String refreshToken = jwtUtils.generateRefreshToken(email);

        assertTrue(jwtUtils.isTokenValid(refreshToken, email));
    }
}