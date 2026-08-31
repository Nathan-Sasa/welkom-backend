package com.nathdev.welkom.dto.event;

import java.time.LocalDateTime;

public record UpdateEventRequest(
        String title,
        String description,
        LocalDateTime dateEventStart,
        LocalDateTime dateEventEnd,
        Integer estimatedGuest,
        String image
) {
}
