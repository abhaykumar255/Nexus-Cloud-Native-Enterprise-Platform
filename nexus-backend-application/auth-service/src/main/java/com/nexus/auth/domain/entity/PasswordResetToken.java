package com.nexus.auth.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Password reset token entity
 */
@Entity
@Table(name = "password_reset_tokens", indexes = {
        @Index(name = "idx_token", columnList = "token"),
        @Index(name = "idx_user_id", columnList = "user_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    
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
    private boolean used = false;
    
    private Instant usedAt;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}

