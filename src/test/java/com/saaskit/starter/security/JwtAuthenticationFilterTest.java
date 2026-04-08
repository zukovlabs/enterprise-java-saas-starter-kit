package com.saaskit.starter.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueChainWithoutAuthenticatingWhenNoAuthHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldContinueChainWithoutAuthenticatingWhenAuthHeaderNotBearer() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic YWxhZGRpbjpvcGVuc2VzYW1l");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldAuthenticateUserWhenTokenIsValid() throws ServletException, IOException {
        String token = "valid.jwt.token";
        String userEmail = "test@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.isRefreshToken(token)).thenReturn(false);
        when(jwtUtils.extractEmail(token)).thenReturn(userEmail);

        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn(userEmail);
        when(mockUserDetails.getAuthorities()).thenReturn(Collections.emptyList());

        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(mockUserDetails);
        when(jwtUtils.isTokenValid(token, userEmail)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(mockUserDetails, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    void shouldNotAuthenticateWhenTokenIsInvalid() throws ServletException, IOException {
        String token = "invalid.jwt.token";
        String userEmail = "test@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.isRefreshToken(token)).thenReturn(false);
        when(jwtUtils.extractEmail(token)).thenReturn(userEmail);

        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn(userEmail);

        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(mockUserDetails);
        when(jwtUtils.isTokenValid(token, userEmail)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldRejectRefreshTokenUsedAsAccessToken() throws ServletException, IOException {
        String refreshToken = "valid.refresh.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + refreshToken);
        when(jwtUtils.isRefreshToken(refreshToken)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        // Refresh token должен быть отклонён — SecurityContext пуст
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        // extractEmail не должен вызываться
        verify(jwtUtils, never()).extractEmail(anyString());
    }
}