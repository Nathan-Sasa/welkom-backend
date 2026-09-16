package com.nathdev.welkom.dto.guest;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateGuestRequest(

        @Schema(
                description = "Nom de l'invité",
                example = "Nathan"
        )
        String firstName,

        @Schema(
                description = "Nom de famille de l'invité",
                example = "Sasa"
        )
        String lastName,

        @Schema(
                description = "E-mail de l'invité",
                example = "nathan@contact.com"
        )
        String email,

        @Schema(
                description = "Numéro de téléphone de l'invité",
                example = "+24389000000000"
        )
        String telephone,

        @Schema(
                description = "Catégorie de l'invité",
                example = "Famille"
        )
        String category
) {
}
