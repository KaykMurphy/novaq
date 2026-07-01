package com.novaq.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductUpdateDTO(

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotBlank
        String brand,

        String imageUrl,

        @NotNull
        Boolean freeShipping,

        @NotNull
        BigDecimal shippingCost


) {
}
