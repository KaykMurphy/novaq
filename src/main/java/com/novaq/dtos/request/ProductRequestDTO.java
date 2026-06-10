package com.novaq.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ProductRequestDTO (

        @NotBlank
        String nome,

        @NotBlank
        String descricao,

        @NotBlank
        String marca,

        @NotNull
        UUID categoriaId,

        @NotEmpty
        @Valid
        List<ProductVariantRequestDTO> variacoes

){
}
