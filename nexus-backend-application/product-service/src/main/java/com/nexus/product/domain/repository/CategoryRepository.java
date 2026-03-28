package com.nexus.product.domain.repository;

import com.nexus.product.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    
    Optional<Category> findBySlug(String slug);
    
    List<Category> findByParentIdIsNull(); // Root categories
    
    List<Category> findByParentId(String parentId);
    
    List<Category> findByCategoryTypeAndIsActiveTrue(String categoryType);
    
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.isActive = true ORDER BY c.displayOrder")
    List<Category> findRootCategoriesOrderByDisplayOrder();
    
    @Query("SELECT c FROM Category c WHERE c.isActive = true ORDER BY c.displayOrder")
    List<Category> findAllActiveOrderByDisplayOrder();
}

