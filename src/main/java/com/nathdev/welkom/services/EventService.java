package com.nathdev.welkom.services;

import com.nathdev.welkom.enums.EventsStatus;
import com.nathdev.welkom.enums.Payment_status;
import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.User;
import com.nathdev.welkom.repositories.EventRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
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

    public void createEvent(
            User user,
            Event event
            ) {

        Optional<Event> getEvent = eventRepository.findById(event.getId());

        if(getEvent.isEmpty()){

            event.setUser(user);

            if (event.getTitle() != null) {
                event.setTitle(event.getTitle());
            }
            if (event.getDescription() != null) {
                event.setDescription(event.getDescription());
            }
//            if (event.getDateEvent() != null) {
//                event.setDateEvent(event.getDateEvent());
//
//            }

//            if (event.getEstimatedGuests() > 0){
//                event.setEstimatedGuests(event.getEstimatedGuests());
//            }


            Integer i = 250;

            event.setEstimatedGuests(i);
            event.setStatus(EventsStatus.PENDING);
            event.setPaymentStatus(Payment_status.PENDING);
            event.setSecureId(generateKeyService.generateShortNumberKey());
            event.setSecurityEventKey(generateKeyService.generateSecureKey());
            eventRepository.save(event);
            return;
        }

        eventRepository.save(event);
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
