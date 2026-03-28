package com.nexus.cart.mapper;

import com.nexus.cart.domain.model.Cart;
import com.nexus.cart.domain.model.CartItem;
import com.nexus.cart.dto.CartDto;
import com.nexus.cart.dto.CartItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Cart entities and DTOs
 */
@Mapper(componentModel = "spring")
public interface CartMapper {
    
    @Mapping(target = "totalItemCount", expression = "java(cart.getTotalItemCount())")
    CartDto toDto(Cart cart);
    
    Cart toEntity(CartDto cartDto);
    
    @Mapping(target = "available", expression = "java(cartItem.isAvailable())")
    CartItemDto toItemDto(CartItem cartItem);
    
    CartItem toItemEntity(CartItemDto cartItemDto);
}

