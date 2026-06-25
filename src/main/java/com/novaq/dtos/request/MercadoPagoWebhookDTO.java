package com.novaq.dtos.request;

public record MercadoPagoWebhookDTO(
        String action,
        MercadoPagoDataDTO data
) {}
