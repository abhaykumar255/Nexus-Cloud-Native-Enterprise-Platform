package com.nexus.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

/**
 * User profile entity with complete user information
 */
@Entity
@Table(name = "user_profiles", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_email", columnList = "email")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId; // Reference to Auth Service user
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false, length = 50)
    private String username;
    
    @Column(length = 100)
    private String firstName;
    
    @Column(length = 100)
    private String lastName;
    
    @Column(length = 20)
    private String phoneNumber;
    
    private LocalDate dateOfBirth;
    
    @Column(length = 500)
    private String bio;
    
    @Column(length = 255)
    private String avatarUrl;
    
    @Column(length = 100)
    private String department;
    
    @Column(length = 100)
    private String jobTitle;
    
    @Column(length = 100)
    private String location;
    
    @Column(length = 10)
    private String timezone;
    
    @Column(length = 10)
    private String language;
    
    @Column(nullable = false)
    @Builder.Default
    private boolean profileComplete = false;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
    
    // User roles - stored as JSON or separate table in production
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_profile_id"))
    @Column(name = "role")
    @Builder.Default
    private java.util.Set<String> roles = new java.util.HashSet<>();
}

