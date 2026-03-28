package com.nexus.payment.mapper;

import com.nexus.payment.domain.entity.Payment;
import com.nexus.payment.dto.PaymentDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    
    PaymentDto toDto(Payment payment);
    
    List<PaymentDto> toDtoList(List<Payment> payments);
}

