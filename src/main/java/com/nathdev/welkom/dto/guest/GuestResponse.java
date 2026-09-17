package com.nathdev.welkom.dto.guest;

import java.time.LocalDateTime;
import java.util.UUID;

public record GuestResponse(
        UUID uuid,
        String firstName,
        String lastName,
        String email,
        String telephone,
        String category,
        UUID tableUuid,
        LocalDateTime addedAt
) {
}
