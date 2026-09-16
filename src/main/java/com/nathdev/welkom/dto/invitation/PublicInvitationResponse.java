package com.nathdev.welkom.dto.invitation;

import com.nathdev.welkom.enums.RsvpStatus;
import com.nathdev.welkom.models.Location;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record PublicInvitationResponse(
        UUID accessToken,
        EventPublicInfo event,
        GuestPublicInfo guest,
        CustomizedTemplatePublicInfo customizedTemplate,
        RsvpStatus rsvpStatus

) {
    public record EventPublicInfo (
            UUID uuid,
            String title,
            String description,
            LocalDateTime dateEventStart,
            LocalDateTime dateEventEnd,
            String image,
            String location
    ){}

    public record GuestPublicInfo (
            UUID uuid,
            String firstName,
            String lastName,
            String category,
            String table
    ) {}

    public record CustomizedTemplatePublicInfo(
            UUID uuid,
            String template
    ){}
}
