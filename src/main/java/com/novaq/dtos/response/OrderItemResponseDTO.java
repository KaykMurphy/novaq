package com.novaq.dtos.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDTO(

        UUID id,
        String nomeProduto,
        String sku,
        Integer quantidade,
        BigDecimal precoComprado, // do price at purchase
        BigDecimal subTotal
) {
}
