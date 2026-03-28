package com.nexus.seller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSellerRequest {
    
    @NotBlank(message = "Store name is required")
    private String storeName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    
    @NotBlank(message = "Business name is required")
    private String businessName;
    
    private String businessType;
    private String gstNumber;
    private String panNumber;
    private String description;
}

