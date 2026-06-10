package com.novaq.dtos.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductVariantRequestDTO(

        @NotBlank
        String sku,

        String cor,

        @NotNull
        @Min(0)
        Integer quantidadeEstoque,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal preco

) {
}
