package com.novaq.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductImageRequestDTO(

        @NotBlank
        String url,

        @NotNull
        Integer position

) {
}
