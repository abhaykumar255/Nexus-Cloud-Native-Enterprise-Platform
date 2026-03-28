package com.nexus.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Email Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    
    private String to;
    private List<String> cc;
    private String subject;
    private String htmlContent;
    private String textContent;
    private String templateId;
}

