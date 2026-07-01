package com.novaq.dtos.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductResponseDTO(

        UUID id,
        String name,
        String description,
        String brand,
        String categoryName,
        String imageUrl,

        List<ProductImageResponseDTO> images,

        Boolean freeShipping,
        BigDecimal shippingCost,

        List<ProductVariantResponseDTO> variations

) {
}
