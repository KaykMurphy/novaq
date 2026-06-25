package com.novaq.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO (

        @NotBlank
        String name
){
}
