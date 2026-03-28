package com.nexus.seller.mapper;

import com.nexus.seller.domain.entity.Seller;
import com.nexus.seller.dto.SellerDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    
    SellerDto toDto(Seller seller);
    
    List<SellerDto> toDtoList(List<Seller> sellers);
}

