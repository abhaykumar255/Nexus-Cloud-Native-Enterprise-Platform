package com.nexus.admin.dto;

import com.nexus.admin.domain.UserModeration.ModerationStatus;
import com.nexus.admin.domain.UserModeration.ViolationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModerationRequest {
    
    @NotNull
    private Long userId;
    
    private String username;
    
    @NotNull
    private ModerationStatus status;
    
    private ViolationType violationType;
    
    private String reason;
    
    private Integer suspensionDays;
    
    @NotNull
    private Long moderatorId;
    
    private String moderatorName;
}

