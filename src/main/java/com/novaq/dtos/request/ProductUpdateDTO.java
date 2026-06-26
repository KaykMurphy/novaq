package com.novaq.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record ProductUpdateDTO(

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotBlank
        String brand
) {
}
