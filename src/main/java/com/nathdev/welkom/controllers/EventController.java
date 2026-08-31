package com.nathdev.welkom.controllers;

import com.nathdev.welkom.dto.event.CreateEventRequest;
import com.nathdev.welkom.dto.event.EventResponse;
import com.nathdev.welkom.dto.event.UpdateEventRequest;
import com.nathdev.welkom.services.EventService;
import com.nathdev.welkom.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<@NotNull EventResponse> createEvent(
            @RequestBody CreateEventRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.create(request));
    }

    @GetMapping("/my-events")
    public ResponseEntity<@NotNull List<EventResponse>> getMyEvents(
            @RequestParam Map<String, String> allRequestParams
    ) {
        return ResponseEntity
                .ok(eventService.getMyEvents());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<@NotNull EventResponse> getEvent(
            @PathVariable UUID uuid
    ) {
        return ResponseEntity
                .ok(eventService.getEvent(uuid));
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<@NotNull EventResponse> updateEvent(
            @PathVariable UUID uuid,
            @RequestBody UpdateEventRequest request
    ) {
        return ResponseEntity
                .ok(eventService.updateEvent(uuid, request));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<@NotNull Void> deleteEvent(
            @PathVariable UUID uuid
    ) {
        eventService.deleteEvent(uuid);
        return ResponseEntity.noContent().build();
    }

}
