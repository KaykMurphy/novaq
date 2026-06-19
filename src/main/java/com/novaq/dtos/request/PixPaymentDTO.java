package com.novaq.dtos.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PixPaymentDTO {
    private BigDecimal amount;
    private String email;
    private String cpf;
    private String orderId;
    private String firstName;
    private String lastName;
}