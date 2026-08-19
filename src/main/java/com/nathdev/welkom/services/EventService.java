package com.nathdev.welkom.services;

import com.nathdev.welkom.components.AuthenticateUser;
import com.nathdev.welkom.dto.event.CreateEventRequest;
import com.nathdev.welkom.dto.event.EventResponse;
import com.nathdev.welkom.enums.EventsStatus;
import com.nathdev.welkom.enums.Payment_status;
import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.User;
import com.nathdev.welkom.repositories.EventRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {
    private EventRepository eventRepository;
    private GenerateKeyService generateKeyService;
    private AuthenticateUser authenticateUser;

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
        event.setSecurityEventKey(generateKeyService.generateShortNumberKey());
        event.setStatus(EventsStatus.PENDING);
        event.setPaymentStatus(Payment_status.PENDING);

        Event saveEvent =  eventRepository.save(event);

        return toResponse(saveEvent);
    }

    public Event updateEvent(String event_id, Map<String, Object> patch) {
        Event existEvent = eventRepository.findBySecureId(event_id)
                .orElseThrow(() -> new RuntimeException("Évenement introuvable"));

        patch.forEach((key, value) -> {
            if (key.equals("name")) {
                Field field = ReflectionUtils.findField(existEvent.getClass(), key);

                if (field != null) {
                    field.setAccessible(true);
                    if (field.getType().equals(LocalDate.class)) {
                        if (value != null) {
                            LocalDate localDate = LocalDate.parse((String) value);
                            ReflectionUtils.setField(field, existEvent, localDate);
                        }
                    }else {
                        ReflectionUtils.setField(field, existEvent, value);
                    }
                }
            }
        });
        return eventRepository.save(existEvent);
    }

    public List<EventResponse> getMyEvents() {
        User user = authenticateUser.getUser();
        return eventRepository.findAllByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ResponseEntity <@NotNull List<Event>> getAllEvent() {
        return new ResponseEntity<>(eventRepository.findAll(), HttpStatus.OK);
    }

    private EventResponse toResponse(Event event) {
        return new  EventResponse(
                event.getUuid(),
                event.getSecureId(),
                event.getTitle(),
                event.getDescription(),
                event.getDateEventStart(),
                event.getDateEventEnd(),
                event.getEstimatedGuests(),
                event.getImage(),
                event.getStatus(),
                event.getPaymentStatus(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }
}
