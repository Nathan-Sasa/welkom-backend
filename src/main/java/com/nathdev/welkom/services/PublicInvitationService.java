package com.nathdev.welkom.services;

import com.nathdev.welkom.dto.invitation.PublicInvitationResponse;
import com.nathdev.welkom.exceptions.event.EventNotFoundException;
import com.nathdev.welkom.exceptions.invitation.InvitationNotFoundException;
import com.nathdev.welkom.models.CustomizedTemplates;
import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.Guest;
import com.nathdev.welkom.models.Invitation;
import com.nathdev.welkom.repositories.InvitationRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublicInvitationService {
    private final InvitationRepository invitationRepository;
    private final QrCodeService qrCodeService;

    public PublicInvitationResponse getInvitation(UUID accessToken){
        Invitation invitation = invitationRepository
                .findByAccessToken(accessToken)
                .orElseThrow(() -> new InvitationNotFoundException(accessToken));

        Event event = invitation.getEvent();

        if (event.getDeletedAt() != null){
            throw new EventNotFoundException(event.getUuid());
        }
        return toPublicResponse(invitation);
    }

    private PublicInvitationResponse toPublicResponse(Invitation invitation) {
        Event event = invitation.getEvent();
        Guest guest = invitation.getGuest();
        CustomizedTemplates customizedTemplates = invitation.getCustomizedTemplate();

        var eventInfo = new PublicInvitationResponse.EventPublicInfo(
                event.getUuid(),
                event.getTitle(),
                event.getDescription(),
                event.getDateEventStart(),
                event.getDateEventEnd(),
                event.getImage(),
                event.getLocation().getAddress() != null ? event.getLocation().getAddress() : null
        );

        var guestInfo = new PublicInvitationResponse.GuestPublicInfo(
                guest.getUuid(),
                guest.getFirstName(),
                guest.getLastName(),
                guest.getCategory(),
                guest.getTables().getTableName()
        );

        var templateInfo = new PublicInvitationResponse.CustomizedTemplatePublicInfo(
                customizedTemplates.getUuid(),
                customizedTemplates.getTemplate().getName()
        );

        return new PublicInvitationResponse(
                invitation.getAccessToken(),
                eventInfo,
                guestInfo,
                templateInfo,
                invitation.getRsvpStatus()
        );
    }

    public byte[] getQRCode(UUID accessToken){
        Invitation invitation = invitationRepository
                .findByAccessToken(accessToken)
                .orElseThrow(() -> new InvitationNotFoundException(accessToken));

        Event event = invitation.getEvent();

        if (event.getDeletedAt() != null){
            throw new EventNotFoundException(event.getUuid());
        }

        return qrCodeService.generateQrCode(invitation.getQrToken());
    }
}
