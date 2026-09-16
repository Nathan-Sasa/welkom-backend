package com.nathdev.welkom.dto.guest;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateGuestRequest(

        @Schema(
                description = "Modifier le Nom de l'invité"
        )
        String firstName,

        @Schema(
                description = "Modifier le nom de la famille de l'invité"
        )
        String lastName,

        @Schema(
                description = "Modifier l'émail de l'invité"
        )
        String email,

        @Schema(
                description = "Modifier le numéro de téléphone de l'invité"
        )
        String telephone,

        @Schema(
                description = "Modifier la catégorie de l'invité"
        )
        String category
) {
}
