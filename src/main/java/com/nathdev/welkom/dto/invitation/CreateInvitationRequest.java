package com.nathdev.welkom.dto.invitation;

import java.util.UUID;

public record CreateInvitationRequest(
        UUID guestUuid
) {
}
