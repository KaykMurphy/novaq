package com.novaq.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CartItemRequestDTO(

        @NotNull
        UUID variantId,

        @NotNull
        @Min(1)
        Integer quantity

) {
}
