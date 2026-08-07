package com.nathdev.welkom.services;

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

    public Event createEvent(
            User user,
            Event event
            ) {

        Optional<Event> getEvent = eventRepository.findById(event.getId());

        if(getEvent.isEmpty()){

            event.setUser(user);
            event.setTitle(event.getTitle());
            event.setDescription(event.getDescription());
            event.setDate_event(event.getDate_event());
            event.setEstimated_guests(event.getEstimated_guests());
            event.setSecureId(generateKeyService.generateShortNumberKey());
            event.setStatus(EventsStatus.PENDING);
            event.setPayment_status(Payment_status.PENDING);
            eventRepository.save(event);

            return event;
        }

        return  eventRepository.save(event);
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

    public ResponseEntity <@NotNull List<Event>> getAllEvent() {
        return new ResponseEntity<>(eventRepository.findAll(), HttpStatus.OK);
    }
}
