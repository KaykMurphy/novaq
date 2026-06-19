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
        log.info("SDK do Mercado Pago inicializado com sucesso.");
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
                .description("Compra na NovaQ")
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

            String copiaECola = payment.getPointOfInteraction().getTransactionData().getQrCode();
            String base64 = payment.getPointOfInteraction().getTransactionData().getQrCodeBase64();

            log.info("PIX gerado com sucesso! ID: {}", payment.getId());

            return new PixResponseDTO(payment.getId(), base64, copiaECola);

        } catch (MPApiException ex) {
            log.error("MercadoPago Error. Status: {}, Content: {}", ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
            throw new RuntimeException("Erro na API do Mercado Pago ao gerar PIX");
        } catch (MPException ex) {
            log.error("Erro interno do SDK: ", ex);
            throw new RuntimeException("Erro interno ao gerar PIX");
        }
    }
}