package com.nexus.inventory.mapper;

import com.nexus.inventory.domain.entity.InventoryStock;
import com.nexus.inventory.dto.InventoryStockDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    
    @Mapping(target = "actualAvailableStock", expression = "java(stock.getActualAvailableStock())")
    InventoryStockDto toDto(InventoryStock stock);
}

