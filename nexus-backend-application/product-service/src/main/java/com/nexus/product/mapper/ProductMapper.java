package com.nexus.product.mapper;

import com.nexus.product.domain.entity.Product;
import com.nexus.product.domain.entity.ProductAttribute;
import com.nexus.product.dto.CreateProductRequest;
import com.nexus.product.dto.ProductAttributeDto;
import com.nexus.product.dto.ProductDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ProductDto toDto(Product product);
    
    List<ProductDto> toDtoList(List<Product> products);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "totalReviews", constant = "0")
    @Mapping(target = "totalSales", constant = "0")
    @Mapping(target = "viewCount", constant = "0")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(CreateProductRequest request);
    
    ProductAttributeDto attributeToDto(ProductAttribute attribute);
    
    List<ProductAttributeDto> attributesToDtos(List<ProductAttribute> attributes);
}

