package com.novaq.dtos.response;

import com.novaq.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(

        UUID id,
        LocalDateTime createdAt,
        OrderStatus status,
        BigDecimal totalValue,
        List<OrderItemResponseDTO> items,

        String qrCodePix,
        String qrCodeBase64,

        UUID userId,
        String userName,


        String paymentId

) {
}
