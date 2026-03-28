package com.nexus.admin.dto;

import com.nexus.admin.domain.ContentModeration.EntityType;
import com.nexus.admin.domain.ContentModeration.ReportReason;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentModerationRequest {
    
    @NotNull
    private EntityType entityType;
    
    @NotNull
    private Long entityId;
    
    @NotNull
    private Long reportedByUserId;
    
    @NotNull
    private ReportReason reason;
    
    private String description;
}

