package com.nathdev.welkom.dto.invitation;

import com.nathdev.welkom.enums.RsvpStatus;
import com.nathdev.welkom.enums.ScanStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record InvitationResponse(
        UUID uuid,
        UUID eventUuid,
        UUID guestUuid,
        UUID customizedTemplateUuid,
        RsvpStatus rsvpStatus,
        ScanStatus scanStatus,
        LocalDateTime scannedAt
) {
}
