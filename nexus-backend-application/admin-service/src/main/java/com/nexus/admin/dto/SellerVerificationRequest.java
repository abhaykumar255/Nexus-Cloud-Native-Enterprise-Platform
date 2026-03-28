package com.nexus.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerVerificationRequest {
    
    @NotNull
    private Long sellerId;
    
    @NotBlank
    private String businessName;
    
    @Email
    @NotBlank
    private String contactEmail;
    
    private String contactPhone;
    
    private String businessRegistrationNumber;
    
    private String taxId;
    
    private String documents; // JSON array of document URLs
    
    private Integer documentCount;
}

