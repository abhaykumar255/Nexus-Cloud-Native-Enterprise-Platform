package com.nexus.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    
    private String id;
    private String userId;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String bio;
    private String avatarUrl;
    private String department;
    private String jobTitle;
    private String location;
    private String timezone;
    private String language;
    private boolean profileComplete;
    private Set<String> roles;
    private Instant createdAt;
    private Instant updatedAt;
}

