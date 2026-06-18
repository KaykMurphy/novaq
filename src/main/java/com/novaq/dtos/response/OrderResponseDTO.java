package com.novaq.dtos.response;

import com.novaq.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(

        UUID id,
        LocalDateTime dataCriacao,
        OrderStatus status,
        BigDecimal valorTotal,
        List<OrderItemResponseDTO> itens

) {
}
