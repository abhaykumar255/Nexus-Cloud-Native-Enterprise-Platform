package com.nexus.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * JWT token generation and validation service
 */
@Slf4j
@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private long expirationMs;
    
    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationMs;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * Generate access token with user ID and roles
     */
    public String generateAccessToken(String userId, String email, List<String> roles) {
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(expirationMs);
        
        return Jwts.builder()
                .subject(userId)
                .claim("email", email)
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey())
                .compact();
    }
    
    /**
     * Generate refresh token
     */
    public String generateRefreshToken(String userId) {
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(refreshExpirationMs);
        
        return Jwts.builder()
                .subject(userId)
                .claim("type", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey())
                .compact();
    }
    
    /**
     * Validate JWT token
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Extract user ID from token
     */
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
    
    /**
     * Get token expiration time
     */
    public long getExpirationMs() {
        return expirationMs;
    }
}

