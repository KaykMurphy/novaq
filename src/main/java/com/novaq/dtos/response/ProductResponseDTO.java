package com.novaq.dtos.response;

import java.util.List;
import java.util.UUID;

public record ProductResponseDTO(

        UUID id,
        String name,
        String description,
        String brand,
        String categoryName,
        List<ProductVariantResponseDTO> variations

) {
}
