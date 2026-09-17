package com.nathdev.welkom.services;

import com.nathdev.welkom.components.AuthenticateUser;
import com.nathdev.welkom.dto.event.CreateEventRequest;
import com.nathdev.welkom.dto.event.EventResponse;
import com.nathdev.welkom.dto.event.SecurityEventKeyResponse;
import com.nathdev.welkom.dto.event.UpdateEventRequest;
import com.nathdev.welkom.enums.EventsStatus;
import com.nathdev.welkom.enums.PaymentStatus;
import com.nathdev.welkom.exceptions.accessDenied.AccessDeniedCustomException;
import com.nathdev.welkom.exceptions.event.EventIllegalCustomException;
import com.nathdev.welkom.exceptions.event.EventNotFoundException;
import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.Location;
import com.nathdev.welkom.models.User;
import com.nathdev.welkom.repositories.EventRepository;
import com.nathdev.welkom.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EventService {
    private EventRepository eventRepository;
    private GenerateKeyService generateKeyService;
    private AuthenticateUser authenticateUser;
    private LocationRepository locationRepository;

    public EventResponse create(
            CreateEventRequest request
    ) {
        User user = authenticateUser.getUser();

        Event event = new Event();

        event.setUser(user);

        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setDateEventEnd(request.dateEventEnd());
        event.setDateEventStart(request.dateEventStart());
        event.setEstimatedGuests(request.estimatedGuests());
        event.setImage(request.image());

        event.setSecureId(generateKeyService.generateShortNumberKey());
//        event.setSecurityEventKey(generateKeyService.generateSecurityAccessKey());
        event.setStatus(EventsStatus.PENDING);
        event.setPaymentStatus(PaymentStatus.PENDING);

        Location location = new Location();
        location.setEvent(event);
        location.setAddress(request.address());
//        locationRepository.save(location);

        event.setLocation(location);
        Event saveEvent =  eventRepository.save(event);

        return toResponse(saveEvent);
    }

    public List<EventResponse> getMyEvents() {
        User user = authenticateUser.getUser();
        return eventRepository.findAllByUserAndDeletedAtIsNull(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public EventResponse getEvent(UUID uuid) {
        User user = authenticateUser.getUser();

        Event event = eventRepository.findByUuidAndDeletedAtIsNull(uuid)
                .orElseThrow(() -> new EventNotFoundException(uuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas !");
        }

        return toResponse(event);
    }

    public EventResponse updateEvent(
            UUID uuid,
            UpdateEventRequest request
    ) {
        User user = authenticateUser.getUser();

        Event event = eventRepository.findByUuidAndDeletedAtIsNull(uuid)
                .orElseThrow(() -> new EventNotFoundException(uuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas !");
        }

        if (request.title() != null) {
            event.setTitle(request.title());
        }
        if (request.description() != null) {
            event.setDescription(request.description());
        }
        if (request.dateEventEnd() != null) {
            event.setDateEventEnd(request.dateEventEnd());
        }
        if (request.dateEventStart() != null) {
            event.setDateEventStart(request.dateEventStart());
        }
        if (request.estimatedGuest() != null) {
            event.setEstimatedGuests(request.estimatedGuest());
        }
        if (request.image() != null) {
            event.setImage(request.image());
        }

        if (request.address() != null) {
            event.getLocation().setAddress(request.address());
        }

        Event updatedEvent = eventRepository.save(event);
        return toResponse(updatedEvent);
    }

    public ResponseEntity <@NotNull List<Event>> getAllEvent() {
        return new ResponseEntity<>(eventRepository.findAll(), HttpStatus.OK);
    }

    private EventResponse toResponse(Event event) {
        return new  EventResponse(
                event.getUuid(),
                event.getTitle(),
                event.getDescription(),
                event.getDateEventStart(),
                event.getDateEventEnd(),
                event.getEstimatedGuests(),
                event.getImage(),
                event.getStatus(),
                event.getPaymentStatus(),
                event.getLocation().getAddress() != null ? event.getLocation().getAddress() : null
        );
    }

    public void deleteEvent(UUID uuid) {
        User user = authenticateUser.getUser();

        Event event = eventRepository.findByUuidAndDeletedAtIsNull(uuid)
                .orElseThrow(() -> new EventNotFoundException(uuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas !");
        }

        event.setDeletedAt(LocalDateTime.now());
        eventRepository.save(event);
    }

    public EventResponse activateEvent(UUID eventUuid) {
        User user = authenticateUser.getUser();

        Event event = eventRepository
                .findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas !");
        }

        if (event.getPaymentStatus() != PaymentStatus.PAYMENT_SUCCESS){
            throw new EventIllegalCustomException("L'événement doit être payé avant son activation.");
        }

        if (event.getSecurityEventKey() == null) {
            event.setSecurityEventKey(generateKeyService.generateSecurityAccessKey());
        }

        return toResponse(eventRepository.save(event));
    }

    public SecurityEventKeyResponse getSecurityEventKey(UUID eventUuid) {
        User user = authenticateUser.getUser();

        Event event = eventRepository
                .findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas !");
        }

        if (event.getPaymentStatus() != PaymentStatus.PAYMENT_SUCCESS) {
            throw new EventIllegalCustomException("Finalisez la paiement de l'événement pour acceder à la clé de sécurité !");
        }

        if (event.getSecurityEventKey() == null) {
            throw new EventIllegalCustomException("La clé de sécurité de l'événement n'a pas encore été générée !");
        }

        return new SecurityEventKeyResponse(event.getSecurityEventKey());
    }

    public  SecurityEventKeyResponse regenerateSecurityEventKey(UUID eventUuid) {
        User user = authenticateUser.getUser();

        Event event = eventRepository
                .findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas !");
        }

        if (event.getPaymentStatus() != PaymentStatus.PAYMENT_SUCCESS) {
            throw new EventIllegalCustomException("Finalisez la paiement de l'événement pour regénérer à la clé de sécurité !");
        }

        String newSecurityEventKey = generateKeyService.generateSecurityAccessKey();

        event.setSecurityEventKey(newSecurityEventKey);
        eventRepository.save(event);
        return new SecurityEventKeyResponse(newSecurityEventKey);
    }
}
