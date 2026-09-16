package com.nathdev.welkom.dto.tables;

import com.nathdev.welkom.models.Guest;

import java.util.List;
import java.util.UUID;

public record TableResponse(
        UUID id,
        String tableName,
        int maxSeats,
        int countSeats,
        List<TableGuestResponse> guest
) {
}
