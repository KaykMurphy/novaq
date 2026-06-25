package com.novaq.controller;


import com.novaq.dtos.request.MercadoPagoWebhookDTO;
import com.novaq.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class WebhookController {

    private final OrderService orderService;

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleMercadoPagoWebhook(@RequestBody MercadoPagoWebhookDTO payload){

        orderService.processPaymentNotification(payload);

        return ResponseEntity.ok().build();

    }

}
