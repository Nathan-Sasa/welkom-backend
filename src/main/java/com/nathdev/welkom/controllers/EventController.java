package com.nathdev.welkom.controllers;

import com.nathdev.welkom.dto.event.CreateEventRequest;
import com.nathdev.welkom.dto.event.EventResponse;
import com.nathdev.welkom.dto.event.SecurityEventKeyResponse;
import com.nathdev.welkom.dto.event.UpdateEventRequest;
import com.nathdev.welkom.services.EventService;
import com.nathdev.welkom.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/events")
@Tag(name = "Evénement", description = "Endpoints pour créer, modifier et lire les détails d'événements")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final UserService userService;

    @PostMapping("/create")
    @Operation(summary = "Créer un nouvel événement")
    public ResponseEntity<@NotNull EventResponse> createEvent(
            @RequestBody CreateEventRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.create(request));
    }

    @GetMapping("/my-events")
    @Operation(summary = "List des événement d'un utilisateur.")
    public ResponseEntity<@NotNull List<EventResponse>> getMyEvents(
//            @RequestParam Map<String, String> allRequestParams
    ) {
        return ResponseEntity
                .ok(eventService.getMyEvents());
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Details d'un événement d'un utilisateur.")
    public ResponseEntity<@NotNull EventResponse> getEvent(
            @PathVariable UUID uuid
    ) {
        return ResponseEntity
                .ok(eventService.getEvent(uuid));
    }

    @PatchMapping("/{uuid}")
    @Operation(summary = "Modification d'un événemement par son propiété")
    public ResponseEntity<@NotNull EventResponse> updateEvent(
            @PathVariable UUID uuid,
            @RequestBody UpdateEventRequest request
    ) {
        return ResponseEntity
                .ok(eventService.updateEvent(uuid, request));
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Suppression d'un événement par son utilisateur.")
    public ResponseEntity<@NotNull Void> deleteEvent(
            @PathVariable UUID uuid
    ) {
        eventService.deleteEvent(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{eventUuid}/activate")
    @Operation(summary = "Activation de l'événement. Temporaire !")
    public EventResponse activateEvent(
            @PathVariable UUID eventUuid
    ) {
        return eventService.activateEvent(eventUuid);
    }

    @GetMapping("/{eventUuid}/security-key")
    @Operation(summary = "L'endpoint de récupération de la clé sécurité d'un événement.")
    public SecurityEventKeyResponse getSecurityEventKey(
            @PathVariable UUID eventUuid
    ){
        return  eventService.getSecurityEventKey(eventUuid);
    }

    @PostMapping("/{eventUuid}/security-key/regenerate")
    @Operation(summary = "Régénération de la clé de sécurité d'un événement.")
    public SecurityEventKeyResponse regenerateSecurityEventKey(
            @PathVariable UUID eventUuid
    ){
        return eventService.regenerateSecurityEventKey(eventUuid);
    }

}
