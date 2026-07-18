package com.novaq.controller;


import com.mercadopago.exceptions.MPInvalidWebhookSignatureException;
import com.mercadopago.webhook.WebhookSignatureValidator;
import com.novaq.dtos.request.MercadoPagoWebhookDTO;
import com.novaq.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class WebhookController {

    private final OrderService orderService;

    @Value("${mp_webhook_secret}")
    private String webhookSecret;

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleMercadoPagoWebhook(

            @RequestBody MercadoPagoWebhookDTO payload,
            @RequestHeader("x-signature") String xSignature,
            @RequestHeader("x-request-id") String xRequestId,
            @RequestParam("data.id") String dataId){

        try {
            WebhookSignatureValidator.validate(xSignature, xRequestId, dataId, webhookSecret);
        } catch (MPInvalidWebhookSignatureException e) {
            log.warn("Tentativa de webhook com assinatura inválida rejeitada.");
            return ResponseEntity.status(401).build(); //  401 Unauthorized
        }

        if(payload == null || payload.data() == null || payload.data().id() == null
                || payload.data().id().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        orderService.processPaymentNotification(payload);

        return ResponseEntity.ok().build();
    }

}
