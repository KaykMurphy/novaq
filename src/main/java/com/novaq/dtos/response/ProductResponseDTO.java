package com.novaq.dtos.response;

import java.util.List;
import java.util.UUID;

public record ProductResponseDTO(

        UUID id,
        String nome,
        String descricao,
        String marca,
        String categoriaNome,
        List<ProductVariantResponseDTO> variacoes

) {
}
