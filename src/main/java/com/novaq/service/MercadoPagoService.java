package com.novaq.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.novaq.dtos.request.PixPaymentDTO;
import com.novaq.dtos.response.PixResponseDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MercadoPagoService {

    @Value("${mp_access_token}")
    private String accessToken;

    @Value("${url}")
    private String url;

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
        log.info("MercadoPago SDK initialized successfully");
    }

    public PixResponseDTO createPixPayment(PixPaymentDTO paymentDTO) {

        PaymentClient client = new PaymentClient();

        IdentificationRequest payeridentification = IdentificationRequest.builder()
                .type("CPF")
                .number(paymentDTO.getCpf())
                .build();

        PaymentPayerRequest payer = PaymentPayerRequest.builder()
                .firstName(paymentDTO.getFirstName())
                .lastName(paymentDTO.getLastName())
                .email(paymentDTO.getEmail())
                .identification(payeridentification)
                .build();

        PaymentCreateRequest createRequest = PaymentCreateRequest.builder()
                .transactionAmount(paymentDTO.getAmount())
                .description("Purchase at NovaQ")
                .paymentMethodId("pix")
                .binaryMode(true)
                .capture(true)
                .externalReference(paymentDTO.getOrderId())
                .statementDescriptor("NOVAD")
                .notificationUrl(this.url + "/api/checkout/webhook")
                .payer(payer)
                .build();

        try {
            Payment payment = client.create(createRequest);

            String copyPaste = Optional.ofNullable(payment.getPointOfInteraction())
                    .map(poi -> poi.getTransactionData())
                    .map(data -> data.getQrCode())
                    .orElseThrow(() -> new RuntimeException("Código Copia e Cola ausente na resposta do Mercado Pago"));

            String base64 = Optional.ofNullable(payment.getPointOfInteraction())
                    .map(poi -> poi.getTransactionData())
                    .map(data -> data.getQrCodeBase64())
                    .orElseThrow(() -> new RuntimeException("QR Code Base64 ausente na resposta do Mercado Pago"));

            log.info("PIX generated successfully! ID: {}", payment.getId());

            return new PixResponseDTO(payment.getId(), base64, copyPaste);


        } catch (MPApiException ex) {
            log.error("MercadoPago error. Status: {}, Content: {}", ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
            throw new RuntimeException("MercadoPago API error when generating PIX", ex);

        } catch (MPException ex) {
            log.error("Internal SDK error: ", ex);
            throw new RuntimeException("Internal error when generating PIX", ex);
        }
    }
}
