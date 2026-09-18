package com.nathdev.welkom.dto.checking;

import io.swagger.v3.oas.annotations.media.Schema;

public record CheckingAccessRequest(
        @Schema(
                description = "Clé de sécurité de l'événement",
                example = "wlk-alphanumeric12",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String securityEventKey
) {}
