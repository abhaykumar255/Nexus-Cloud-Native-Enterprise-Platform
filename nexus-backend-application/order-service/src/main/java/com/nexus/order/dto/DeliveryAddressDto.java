package com.nexus.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressDto {
    
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phone;
    private Double latitude;
    private Double longitude;
    private String instructions;
}

