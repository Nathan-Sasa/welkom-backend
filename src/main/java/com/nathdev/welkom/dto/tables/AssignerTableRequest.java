package com.nathdev.welkom.dto.tables;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record AssignerTableRequest(
        @Schema(
                description = "Nom de la table",
                example = "Orléan",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String tableName,

        @Schema(
                description = "invité uuid",
                example = "e06e6cd2-a4ec-4ecd-b1b8-e46091873497"
        )
        UUID guestUuid
) {
}
