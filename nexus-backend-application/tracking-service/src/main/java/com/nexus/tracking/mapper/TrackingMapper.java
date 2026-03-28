package com.nexus.tracking.mapper;

import com.nexus.tracking.domain.entity.TrackingLocation;
import com.nexus.tracking.dto.TrackingLocationDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrackingMapper {
    
    TrackingLocationDto toDto(TrackingLocation location);
    
    List<TrackingLocationDto> toDtoList(List<TrackingLocation> locations);
}

