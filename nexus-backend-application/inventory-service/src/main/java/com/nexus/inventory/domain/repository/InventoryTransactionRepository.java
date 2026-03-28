package com.nexus.inventory.domain.repository;

import com.nexus.inventory.domain.entity.InventoryTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, String> {
    
    /**
     * Find transactions by product ID
     */
    Page<InventoryTransaction> findByProductId(String productId, Pageable pageable);
    
    /**
     * Find transactions by order ID
     */
    List<InventoryTransaction> findByOrderId(String orderId);
    
    /**
     * Find transactions by seller ID
     */
    Page<InventoryTransaction> findBySellerId(String sellerId, Pageable pageable);
    
    /**
     * Find transactions by type
     */
    Page<InventoryTransaction> findByTransactionType(
            InventoryTransaction.TransactionType transactionType, 
            Pageable pageable);
    
    /**
     * Find transactions within date range
     */
    @Query("SELECT t FROM InventoryTransaction t WHERE t.productId = :productId " +
           "AND t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
    List<InventoryTransaction> findByProductIdAndDateRange(
            @Param("productId") String productId,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);
}

