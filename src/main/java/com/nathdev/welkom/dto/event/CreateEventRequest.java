package com.nathdev.welkom.dto.event;

import java.time.LocalDateTime;

public record CreateEventRequest(
        String title,
        String description,
        LocalDateTime dateEventStart,
        LocalDateTime dateEventEnd,
        Integer estimatedGuests,
        String image
) {
}
