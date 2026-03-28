package com.nexus.inventory.domain.repository;

import com.nexus.inventory.domain.entity.InventoryStock;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryStockRepository extends JpaRepository<InventoryStock, String> {
    
    /**
     * Find inventory by product ID with pessimistic lock for updates
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM InventoryStock i WHERE i.productId = :productId")
    Optional<InventoryStock> findByProductIdWithLock(@Param("productId") String productId);
    
    /**
     * Find inventory by product ID
     */
    Optional<InventoryStock> findByProductId(String productId);
    
    /**
     * Find inventory by product and location
     */
    Optional<InventoryStock> findByProductIdAndWarehouseLocation(String productId, String warehouseLocation);
    
    /**
     * Find all inventory for a seller
     */
    Page<InventoryStock> findBySellerId(String sellerId, Pageable pageable);
    
    /**
     * Find low stock items
     */
    @Query("SELECT i FROM InventoryStock i WHERE i.lowStockAlert = true")
    List<InventoryStock> findLowStockItems();
    
    /**
     * Find low stock items for a seller
     */
    @Query("SELECT i FROM InventoryStock i WHERE i.sellerId = :sellerId AND i.lowStockAlert = true")
    List<InventoryStock> findLowStockItemsBySeller(@Param("sellerId") String sellerId);
    
    /**
     * Find out of stock items
     */
    @Query("SELECT i FROM InventoryStock i WHERE i.outOfStock = true")
    List<InventoryStock> findOutOfStockItems();
    
    /**
     * Check if product has sufficient stock
     */
    @Query("SELECT CASE WHEN (i.totalStock - i.reservedStock - i.softReservedStock) >= :quantity THEN true ELSE false END " +
           "FROM InventoryStock i WHERE i.productId = :productId")
    boolean hasAvailableStock(@Param("productId") String productId, @Param("quantity") Integer quantity);
    
    /**
     * Get available stock quantity for a product
     */
    @Query("SELECT (i.totalStock - i.reservedStock - i.softReservedStock) " +
           "FROM InventoryStock i WHERE i.productId = :productId")
    Optional<Integer> getAvailableStock(@Param("productId") String productId);
    
    /**
     * Find products by multiple IDs
     */
    List<InventoryStock> findByProductIdIn(List<String> productIds);
}

