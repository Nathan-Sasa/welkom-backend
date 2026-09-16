package com.nathdev.welkom.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description ="Données requises pour créer un événement")
public record CreateEventRequest(
        @Schema(
                description = "Le titre de l'événement",
                example = "Mariage Lucie et John",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String title,

        @Schema(
                description = "Description détaillée",
                example = "Cérémonie coutumière de Lucie et John",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String description,

        @Schema(
                description = "Date et heure du début de l'événement",
                example = "26/10/206 18:30",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        LocalDateTime dateEventStart,

        @Schema(
                description = "Date et heure de la fin de l'événement",
                example = "26/10/206 23:00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        LocalDateTime dateEventEnd,

        @Schema(
                description = "Nombre estimé d'invités",
                example = "240",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer estimatedGuests,

        @Schema(
                description = "Insérer l'url de l'image couveture de l'événement"
        )
        String image,

        @Schema(
                description = "Entrer le lieu de l'événement.",
                example = "Av. Liberation (ex 24 Novembre) nº139, Gombé/Kinshasa"
        )
        String address
) {
}
