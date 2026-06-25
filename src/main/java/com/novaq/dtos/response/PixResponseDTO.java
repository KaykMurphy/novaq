package com.novaq.dtos.response;

public record PixResponseDTO(
        Long paymentId,
        String qrCodeBase64,
        String qrCodeCopyPaste
) {
}
