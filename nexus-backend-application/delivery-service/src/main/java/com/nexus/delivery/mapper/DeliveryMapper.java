package com.nexus.delivery.mapper;

import com.nexus.delivery.domain.entity.Delivery;
import com.nexus.delivery.dto.DeliveryDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    
    DeliveryDto toDto(Delivery delivery);
    
    List<DeliveryDto> toDtoList(List<Delivery> deliveries);
}

