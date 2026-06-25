package com.novaq.dtos.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDTO(

        UUID id,
        String productName,
        String sku,
        Integer quantity,
        BigDecimal purchasePrice,
        BigDecimal subTotal
) {
}
