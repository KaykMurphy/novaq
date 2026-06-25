package com.novaq.dtos.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponseDTO(
        UUID id,
        UUID variant,
        String productName,
        String color,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subTotal
) {
}
