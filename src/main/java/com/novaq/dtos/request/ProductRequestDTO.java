package com.novaq.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ProductRequestDTO (

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotBlank
        String brand,

        @NotNull
        UUID categoryId,

        @NotEmpty
        @Valid
        List<ProductVariantRequestDTO> variations

){
}
