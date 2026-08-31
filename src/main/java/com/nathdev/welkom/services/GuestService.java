package com.nathdev.welkom.services;

import com.nathdev.welkom.components.AuthenticateUser;
import com.nathdev.welkom.dto.guest.CreateGuestRequest;
import com.nathdev.welkom.dto.guest.GuestResponse;
import com.nathdev.welkom.dto.guest.UpdateGuestRequest;
import com.nathdev.welkom.exceptions.accessDenied.AccessDeniedCustomException;
import com.nathdev.welkom.exceptions.event.EventNotFoundException;
import com.nathdev.welkom.exceptions.guest.GuestNotFoundException;
import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.Guest;
import com.nathdev.welkom.models.User;
import com.nathdev.welkom.repositories.EventRepository;
import com.nathdev.welkom.repositories.GuestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GuestService {

    private AuthenticateUser authenticateUser;
    private GuestRepository guestRepository;
    private EventRepository eventRepository;

    public GuestResponse create(
            UUID eventUuid,
            CreateGuestRequest request
    ) {
        User user = authenticateUser.getUser();

        Event event = eventRepository
                .findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw  new AccessDeniedCustomException("Cet événement ne vous appartient pas");
        }

        Guest guest = new Guest();

        guest.setFirstName(request.firstName());
        guest.setLastName(request.lastName());
        guest.setEmail(request.email());
        guest.setTelephone(request.telephone());
        guest.setCategory(request.category());
        guest.setEvent(event);

        Guest saved = guestRepository.save(guest);

        return toResponse(saved);
    }

    public List<GuestResponse> getAllByEvent(UUID eventUuid) {
        User user = authenticateUser.getUser();

        Event event = eventRepository
                .findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas");
        }

        return guestRepository.findAllByEventAndDeletedAtIsNull(event)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public GuestResponse getByUuid(UUID eventUuid,UUID uuid) {
        User user = authenticateUser.getUser();

        Guest guest = guestRepository.findByUuidAndDeletedAtIsNull(uuid)
                .orElseThrow(() -> new EventNotFoundException(uuid));

        Event event = eventRepository.findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        Event eventOfGuest = guest.getEvent();

        if (eventOfGuest.getDeletedAt() != null) {
            throw new EventNotFoundException(eventOfGuest.getUuid());
        }

        if (!eventOfGuest.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas");
        }

        if (!event.getGuests().contains(guest)) {
            throw new GuestNotFoundException(guest.getUuid());
        }

        return toResponse(guest);
    }

    public GuestResponse update(
            UUID eventUuid,
            UUID uuid,
            UpdateGuestRequest request
    ) {
        User user = authenticateUser.getUser();

        Guest guest = guestRepository.findByUuidAndDeletedAtIsNull(uuid)
                .orElseThrow(() -> new GuestNotFoundException(uuid));

        Event event = eventRepository.findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        Event eventOfGuest = guest.getEvent();

        if (eventOfGuest.getDeletedAt() != null) {
            throw new EventNotFoundException(eventOfGuest.getUuid());
        }

        if (!eventOfGuest.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas");
        }

        if (!event.getGuests().contains(guest)) {
            throw new GuestNotFoundException(guest.getUuid());
        }

        if (request.firstName() != null) {
            guest.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            guest.setLastName(request.lastName());
        }
        if (request.email() != null) {
            guest.setEmail(request.email());
        }
        if (request.telephone() != null) {
            guest.setTelephone(request.telephone());
        }
        if (request.category() != null) {
            guest.setCategory(request.category());
        }

        Guest updated = guestRepository.save(guest);
        return toResponse(updated);
    }

    public void delete(UUID eventUuid, UUID uuid) {
        User user = authenticateUser.getUser();

        Guest guest = guestRepository.findByUuidAndDeletedAtIsNull(uuid)
                .orElseThrow(() -> new GuestNotFoundException(uuid));

        Event event = eventRepository.findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        Event eventOfGuest = guest.getEvent();

        if (eventOfGuest.getDeletedAt() != null) {
            throw new EventNotFoundException(eventOfGuest.getUuid());
        }

        if (!eventOfGuest.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas");
        }

        if (!event.getGuests().contains(guest)) {
            throw new GuestNotFoundException(guest.getUuid());
        }

        guest.setDeletedAt(LocalDateTime.now());
        guestRepository.save(guest);
    }



    private GuestResponse toResponse(Guest guest){
        return new GuestResponse(
                guest.getUuid(),
                guest.getFirstName(),
                guest.getLastName(),
                guest.getEmail(),
                guest.getTelephone(),
                guest.getCategory(),
                guest.getTables() != null ? guest.getTables().getUuid() : null,
                guest.getCreatedAt(),
                guest.getUpdatedAt()
        );
    }
}
