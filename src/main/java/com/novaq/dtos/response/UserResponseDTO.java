package com.novaq.dtos.response;

import com.novaq.enums.UserRole;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponseDTO(

        UUID id,
        String name,
        String email,

        Set<UserRole> roles,

        boolean active,
        LocalDateTime createdAt

) {
}
