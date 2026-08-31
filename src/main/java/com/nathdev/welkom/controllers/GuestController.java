package com.nathdev.welkom.controllers;

import com.nathdev.welkom.dto.guest.CreateGuestRequest;
import com.nathdev.welkom.dto.guest.GuestResponse;
import com.nathdev.welkom.dto.guest.UpdateGuestRequest;
import com.nathdev.welkom.services.GuestService;
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
@RequiredArgsConstructor
public class GuestController {
    private final GuestService guestService;

    @PostMapping("/{eventUuid}/guests")
    public ResponseEntity<@NotNull GuestResponse> create(
            @PathVariable UUID eventUuid,
            @RequestBody CreateGuestRequest request
            ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(guestService.create(eventUuid, request));
    }

    @GetMapping("/{eventUuid}/guests")
    public ResponseEntity<@NotNull List<GuestResponse>> getAllByEvent(
            @PathVariable UUID eventUuid
    ) {
        return ResponseEntity.ok(guestService.getAllByEvent(eventUuid));
    }

    @GetMapping("/{eventUuid}/guests/{uuid}")
    public ResponseEntity<@NotNull GuestResponse> getByUuid(
            @PathVariable UUID eventUuid,
            @PathVariable UUID uuid
    ){
        return ResponseEntity.ok(guestService.getByUuid(eventUuid,uuid));
    }

    @PatchMapping("/{eventUuid}/guests/{uuid}")
    public ResponseEntity<@NotNull GuestResponse> update(
            @PathVariable UUID eventUuid,
            @PathVariable UUID uuid,
            @RequestBody UpdateGuestRequest request
    ){
        return ResponseEntity.ok(guestService.update(
                eventUuid,
                uuid,
                request));
    }

    @DeleteMapping("/{eventUuid}/guests/{uuid}")
    public ResponseEntity<@NotNull Void> delete(
            @PathVariable UUID eventUuid,
            @PathVariable UUID uuid
    ){
        guestService.delete(eventUuid, uuid);
        return ResponseEntity.noContent().build();
    }
}
