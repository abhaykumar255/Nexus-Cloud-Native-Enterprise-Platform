package com.nexus.auth.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Refresh token entity for JWT token rotation
 */
@Entity
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "idx_token", columnList = "token"),
        @Index(name = "idx_user_id", columnList = "user_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true)
    private String token;
    
    @Column(nullable = false, name = "user_id")
    private String userId;
    
    @Column(nullable = false)
    private Instant expiresAt;
    
    @Column(nullable = false)
    @Builder.Default
    private boolean revoked = false;
    
    private Instant revokedAt;
    
    private String deviceInfo;
    
    private String ipAddress;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}

