package com.novaq.dtos.response;

import java.util.UUID;

public record UserResponseDTO(

        UUID id,
        String nome,
        String email


) {
}

