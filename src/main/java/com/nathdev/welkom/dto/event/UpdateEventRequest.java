package com.nathdev.welkom.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UpdateEventRequest(
        @Schema(
                description = "Modifier le titre de l'événement"
        )
        String title,

        @Schema(
                description = "Modifier la description de l'événement"
        )
        String description,

        @Schema(
                description = "Modifier la date et l'heure du début de l'événement"
        )
        LocalDateTime dateEventStart,

        @Schema(
                description = "Modifier la date et l'heure de la fin de l'événement"
        )
        LocalDateTime dateEventEnd,

        @Schema(
                description = "modifier le nomber estimatif de l'événement"
        )
        Integer estimatedGuest,

        @Schema(
                description = "Modifier l'image de la couture de l'événement"
        )
        String image,

        @Schema(
                description = "Modifier l'address de l'événement"
        )
        String address

) {
}
