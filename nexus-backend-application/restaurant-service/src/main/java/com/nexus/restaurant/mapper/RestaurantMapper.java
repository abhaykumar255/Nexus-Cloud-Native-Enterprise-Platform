package com.nexus.restaurant.mapper;

import com.nexus.restaurant.domain.entity.Restaurant;
import com.nexus.restaurant.dto.RestaurantDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    
    RestaurantDto toDto(Restaurant restaurant);
    
    List<RestaurantDto> toDtoList(List<Restaurant> restaurants);
}

