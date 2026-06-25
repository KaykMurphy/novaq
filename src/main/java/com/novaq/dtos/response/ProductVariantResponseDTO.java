package com.novaq.dtos.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductVariantResponseDTO(

        UUID id,
        String sku,
        String color,
        Integer stockQuantity,
        BigDecimal price

) {
}
