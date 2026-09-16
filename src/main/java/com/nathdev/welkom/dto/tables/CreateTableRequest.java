package com.nathdev.welkom.dto.tables;

import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.Guest;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CreateTableRequest(
        @Schema(
                description = "Nom de la table à créer",
                example = "Louisiane",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String tableName,
        @Schema(
                description = "Les places maximum que peut avoir une table.",
                example = "8"
        )
        int maxSeats
) {
}
