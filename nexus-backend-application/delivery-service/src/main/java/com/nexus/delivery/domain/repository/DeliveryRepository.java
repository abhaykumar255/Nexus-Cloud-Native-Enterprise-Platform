package com.nexus.delivery.domain.repository;

import com.nexus.common.enums.DeliveryStatus;
import com.nexus.delivery.domain.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, String> {
    
    Optional<Delivery> findByOrderId(String orderId);
    
    List<Delivery> findByDeliveryPartnerId(String deliveryPartnerId);
    
    List<Delivery> findByStatus(DeliveryStatus status);
    
    List<Delivery> findByDeliveryPartnerIdAndStatus(String partnerId, DeliveryStatus status);
}

