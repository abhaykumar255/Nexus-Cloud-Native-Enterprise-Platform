package com.nexus.order.dto;

import com.nexus.common.enums.OrderType;
import com.nexus.common.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    
    @NotNull(message = "Order type is required")
    private OrderType type;
    
    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    private List<OrderItemRequest> items;
    
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
    
    @Valid
    @NotNull(message = "Delivery address is required")
    private DeliveryAddressRequest deliveryAddress;
    
    private String couponCode;
    
    private String notes;
}

