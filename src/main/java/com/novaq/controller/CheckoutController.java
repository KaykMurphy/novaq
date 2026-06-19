package com.novaq.controller;

import com.novaq.dtos.request.PixPaymentDTO;
import com.novaq.dtos.response.PixResponseDTO;
import com.novaq.service.MercadoPagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
@Slf4j
public class CheckoutController {

    private final MercadoPagoService mercadoPagoService;

    @PostMapping("/pix")
    public ResponseEntity<PixResponseDTO> gerarPix(@RequestBody PixPaymentDTO paymentDTO) {

        PixResponseDTO response = mercadoPagoService.createPixPayment(paymentDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receberNotificacao(@RequestBody Map<String, Object> payload) {
        log.info("Notificação recebida do Mercado Pago: {}", payload);

        return ResponseEntity.ok("Notificação recebida");
    }
}