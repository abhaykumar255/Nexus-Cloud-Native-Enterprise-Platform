package com.nexus.review.mapper;

import com.nexus.review.domain.entity.Review;
import com.nexus.review.dto.ReviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper {
    
    ReviewDto toDto(Review review);
    
    Review toEntity(ReviewDto reviewDto);
}

