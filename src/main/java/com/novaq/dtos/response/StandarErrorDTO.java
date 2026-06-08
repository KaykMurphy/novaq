package com.novaq.dtos.response;

import java.time.LocalDateTime;

public record StandarErrorDTO(

        LocalDateTime timestamp,
        Integer status, //400, 404, 500...
        String message,
        String path // ex. api/auth...

) {}
