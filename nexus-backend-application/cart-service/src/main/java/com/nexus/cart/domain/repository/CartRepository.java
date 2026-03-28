package com.nexus.cart.domain.repository;

import com.nexus.cart.domain.model.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Redis Repository for Cart
 */
@Repository
public interface CartRepository extends CrudRepository<Cart, String> {
    
    Optional<Cart> findByUserId(String userId);
    
    Optional<Cart> findByGuestId(String guestId);
}

