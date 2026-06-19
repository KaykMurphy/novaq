package com.novaq.dtos.response;

public record PixResponseDTO(
        Long idPagamento,
        String qrCodeBase64,
        String qrCodeCopiaECola
) {
}
