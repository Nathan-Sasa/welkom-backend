package com.nathdev.welkom.dto.invitation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CreateInvitationRequest(
        @Schema(
                description = "L'uuid de l'invité",
                example = "307400c-b0f6-413b-b30d-ab1da62b28fb",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID guestUuid
) {
}
