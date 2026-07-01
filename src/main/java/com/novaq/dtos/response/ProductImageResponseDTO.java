package com.novaq.dtos.response;

import java.util.UUID;

public record ProductImageResponseDTO(

        UUID id,
        String url,
        Integer position

) {
}
