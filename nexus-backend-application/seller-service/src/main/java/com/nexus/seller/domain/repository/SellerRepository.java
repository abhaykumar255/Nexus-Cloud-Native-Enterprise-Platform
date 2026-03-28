package com.nexus.seller.domain.repository;

import com.nexus.common.enums.SellerStatus;
import com.nexus.seller.domain.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, String> {
    
    Optional<Seller> findByUserId(String userId);
    
    Optional<Seller> findByEmail(String email);
    
    List<Seller> findByStatus(SellerStatus status);
    
    boolean existsByEmail(String email);
    
    boolean existsByUserId(String userId);
}

