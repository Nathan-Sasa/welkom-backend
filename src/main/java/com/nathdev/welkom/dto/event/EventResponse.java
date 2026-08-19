package com.nathdev.welkom.dto.event;

import com.nathdev.welkom.enums.EventsStatus;
import com.nathdev.welkom.enums.Payment_status;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponse(
        UUID uuid,
        String secureId,
        String title,
        String description,
        LocalDateTime dateEventStart,
        LocalDateTime dateEventEnd,
        Integer estimatedGuests,
        String image,
        EventsStatus eventStatus,
        Payment_status paymentStatus,
        LocalDateTime createdAt,
        LocalDateTime updateAt
) {
}
