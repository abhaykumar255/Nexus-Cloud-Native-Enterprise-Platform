package com.nexus.product.domain.repository;

import com.nexus.common.enums.ProductStatus;
import com.nexus.product.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    
    Optional<Product> findBySku(String sku);
    
    Page<Product> findBySellerId(String sellerId, Pageable pageable);
    
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);
    
    Page<Product> findByProductType(String productType, Pageable pageable);
    
    Page<Product> findByCategoryId(String categoryId, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.status = :status AND p.productType = :productType")
    Page<Product> findByStatusAndProductType(
            @Param("status") ProductStatus status,
            @Param("productType") String productType,
            Pageable pageable
    );
    
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.brand) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Product> searchProducts(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE " +
           "p.status = :status AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:productType IS NULL OR p.productType = :productType)")
    Page<Product> findByFilters(
            @Param("status") ProductStatus status,
            @Param("categoryId") String categoryId,
            @Param("minPrice") java.math.BigDecimal minPrice,
            @Param("maxPrice") java.math.BigDecimal maxPrice,
            @Param("productType") String productType,
            Pageable pageable
    );
    
    List<Product> findByIsFeaturedTrue();
    
    List<Product> findByIsTrendingTrue();
    
    @Query("SELECT p FROM Product p WHERE p.status = 'PUBLISHED' ORDER BY p.totalSales DESC")
    List<Product> findTopSellingProducts(Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.status = 'PUBLISHED' ORDER BY p.createdAt DESC")
    List<Product> findNewArrivals(Pageable pageable);
}

