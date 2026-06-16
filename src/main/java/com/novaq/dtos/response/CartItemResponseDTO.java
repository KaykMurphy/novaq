package com.novaq.dtos.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponseDTO(
        UUID id,
        UUID variant,
        String nomeProduto,
        String cor,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subTotal
) {
}
