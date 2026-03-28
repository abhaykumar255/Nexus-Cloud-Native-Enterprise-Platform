package com.nexus.user.mapper;

import com.nexus.user.domain.entity.UserProfile;
import com.nexus.user.dto.UserProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for UserProfile entity and DTO
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {
    
    UserProfileDto toDto(UserProfile userProfile);
    
    UserProfile toEntity(UserProfileDto userProfileDto);
}

