package com.novaq.dtos.response;

public record ViaCepResponseDTO(
        String postalCode,
        String street,
        String complement,
        String neighborhood,
        String city,
        String state
) {
}
