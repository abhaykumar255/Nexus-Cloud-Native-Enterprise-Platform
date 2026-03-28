package com.nexus.auth.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * User entity for authentication
 * Minimal user data - full profile is in User Service
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_username", columnList = "username")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(nullable = false)
    private String passwordHash;
    
    @Column(nullable = false)
    @Builder.Default
    private boolean emailVerified = false;
    
    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
    
    @Column(nullable = false)
    @Builder.Default
    private boolean locked = false;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AuthProvider authProvider = AuthProvider.LOCAL;
    
    private String providerId; // External provider user ID (Google, etc.)
    
    @Column(nullable = false)
    @Builder.Default
    private int failedLoginAttempts = 0;
    
    private Instant lastLoginAt;
    
    private Instant lockedUntil;
    
    private Instant passwordChangedAt;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
    
    public enum AuthProvider {
        LOCAL,
        GOOGLE,
        GITHUB
    }
}

