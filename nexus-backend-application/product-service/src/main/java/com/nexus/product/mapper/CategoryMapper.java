package com.nexus.product.mapper;

import com.nexus.product.domain.entity.Category;
import com.nexus.product.dto.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    
    @Mapping(target = "parentId", source = "parent.id")
    CategoryDto toDto(Category category);
    
    List<CategoryDto> toDtoList(List<Category> categories);
}

