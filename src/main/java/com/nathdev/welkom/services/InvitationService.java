package com.nathdev.welkom.services;

import com.nathdev.welkom.components.AuthenticateUser;
import com.nathdev.welkom.dto.invitation.CreateInvitationRequest;
import com.nathdev.welkom.dto.invitation.InvitationResponse;
import com.nathdev.welkom.enums.RsvpStatus;
import com.nathdev.welkom.enums.ScanStatus;
import com.nathdev.welkom.exceptions.accessDenied.AccessDeniedCustomException;
import com.nathdev.welkom.exceptions.customizedTemplate.CustomizedTemplateNotFoundException;
import com.nathdev.welkom.exceptions.event.EventNotFoundException;
import com.nathdev.welkom.exceptions.guest.GuestNotFoundException;
import com.nathdev.welkom.exceptions.invitation.InvitationAlreadyExistsException;
import com.nathdev.welkom.exceptions.invitation.InvitationNotFoundException;
import com.nathdev.welkom.models.*;
import com.nathdev.welkom.repositories.CustomizedTemplatesRepository;
import com.nathdev.welkom.repositories.EventRepository;
import com.nathdev.welkom.repositories.GuestRepository;
import com.nathdev.welkom.repositories.InvitationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class InvitationService {

    private AuthenticateUser  authenticateUser;
    private InvitationRepository invitationRepository;
    private EventRepository eventRepository;
    private CustomizedTemplatesRepository customizedTemplatesRepository;
    private GuestRepository guestRepository;

    public InvitationResponse create (
            UUID eventUuid,
            CreateInvitationRequest request
    ){
        User user = authenticateUser.getUser();

        Event event = eventRepository
                .findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        if (!event.getUser().getId().equals(user.getId())){
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas");
        }

        CustomizedTemplates customizedTemplates = customizedTemplatesRepository
                .findByEvent(event)
                .orElseThrow(() -> new CustomizedTemplateNotFoundException(eventUuid));

        Guest guest = guestRepository
                .findByUuidAndDeletedAtIsNull(request.guestUuid())
                .orElseThrow(() -> new GuestNotFoundException(request.guestUuid()));

        if (!guest.getEvent().getId().equals(event.getId())){
            throw new AccessDeniedCustomException("Cet invité n'appartient pas à cet événement");
        }

        if (invitationRepository.findByGuest(guest).isPresent()){
            throw new InvitationAlreadyExistsException();
        }

        Invitation invitation = new Invitation();

        invitation.setEvent(event);
        invitation.setGuest(guest);
        invitation.setCustomizedTemplate(customizedTemplates);
        invitation.setRsvpStatus(RsvpStatus.PENDING);
        invitation.setScanStatus(ScanStatus.PENDING);

        Invitation saved = invitationRepository.save(invitation);

        return toResponse(saved);
    }

    public List<InvitationResponse> getAllByEvent(UUID eventUuid) {
        User user = authenticateUser.getUser();

        Event event = eventRepository
                .findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        if (!event.getUser().getId().equals(user.getId())){
            throw new AccessDeniedCustomException("Cet événetment ne vous appartient pas");
        }

        return invitationRepository
                .findAllByEvent(event)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    
    public InvitationResponse findByUuid(UUID uuid) {
        User user = authenticateUser.getUser();

        Invitation invitation = invitationRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new InvitationNotFoundException(uuid));

        Event event = invitation.getEvent();

        if (event.getDeletedAt() != null){
            throw  new EventNotFoundException(event.getUuid());
        }
         if (!event.getUser().getId().equals(user.getId())){
             throw new AccessDeniedCustomException("Cet invitation ne vous apparient pas");
         }

         return toResponse(invitation);
    }

    private InvitationResponse toResponse(Invitation invitation){
        return new InvitationResponse(
                invitation.getUuid(),
                invitation.getEvent().getUuid(),
                invitation.getGuest().getUuid(),
                invitation.getCustomizedTemplate().getUuid(),
                invitation.getRsvpStatus(),
                invitation.getScanStatus(),
                invitation.getScannedAt(),
                invitation.getAccessToken()
        );
    }
}
