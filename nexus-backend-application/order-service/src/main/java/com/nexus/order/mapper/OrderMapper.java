package com.nexus.order.mapper;

import com.nexus.order.domain.entity.Order;
import com.nexus.order.domain.entity.OrderItem;
import com.nexus.order.dto.DeliveryAddressDto;
import com.nexus.order.dto.OrderDto;
import com.nexus.order.dto.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    
    @Mapping(target = "deliveryAddress", expression = "java(toDeliveryAddressDto(order))")
    OrderDto toDto(Order order);
    
    List<OrderDto> toDtoList(List<Order> orders);
    
    OrderItemDto toItemDto(OrderItem item);
    
    List<OrderItemDto> toItemDtoList(List<OrderItem> items);
    
    default DeliveryAddressDto toDeliveryAddressDto(Order order) {
        return DeliveryAddressDto.builder()
                .address(order.getDeliveryAddress())
                .city(order.getDeliveryCity())
                .state(order.getDeliveryState())
                .postalCode(order.getDeliveryPostalCode())
                .country(order.getDeliveryCountry())
                .phone(order.getDeliveryPhone())
                .latitude(order.getDeliveryLatitude())
                .longitude(order.getDeliveryLongitude())
                .instructions(order.getDeliveryInstructions())
                .build();
    }
}

