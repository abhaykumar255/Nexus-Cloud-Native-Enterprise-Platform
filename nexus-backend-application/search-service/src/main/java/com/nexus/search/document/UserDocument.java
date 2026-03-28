package com.nexus.search.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User document for Elasticsearch
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDocument {

    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String department;
    private String position;
    private List<String> roles;
    private String status;
    private LocalDateTime createdAt;
}

