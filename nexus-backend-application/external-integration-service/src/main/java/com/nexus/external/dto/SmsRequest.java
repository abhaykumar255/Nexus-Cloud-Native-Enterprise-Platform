package com.nexus.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SMS Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequest {
    
    private String to;
    private String message;
}

