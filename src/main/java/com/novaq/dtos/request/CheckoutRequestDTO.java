package com.novaq.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckoutRequestDTO(

        @NotBlank
        String cep,

        @NotNull
        Integer numero,

        String complemento


) {
}
