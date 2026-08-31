package com.nathdev.welkom.controllers;

import com.nathdev.welkom.dto.invitation.CreateInvitationRequest;
import com.nathdev.welkom.dto.invitation.InvitationResponse;
import com.nathdev.welkom.repositories.InvitationRepository;
import com.nathdev.welkom.services.InvitationService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class InvitationController {
    private final InvitationService invitationService;

    @PostMapping("/{eventUuid}/invitations")
    public ResponseEntity<@NotNull InvitationResponse> create (
            @PathVariable UUID eventUuid,
            @RequestBody CreateInvitationRequest request
            ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(invitationService.create(eventUuid, request));
    }

    @GetMapping("/{eventUuid}/invitations")
    public ResponseEntity<@NotNull List<InvitationResponse>> getAll (
            @PathVariable UUID eventUuid
    ){
        return ResponseEntity.ok(invitationService.getAllByEvent(eventUuid));
    }

    @GetMapping("/invitations/{uuid}")
    public ResponseEntity<@NotNull InvitationResponse> get (
            @PathVariable UUID uuid
    ){
        return ResponseEntity.ok(invitationService.findByUuid(uuid));
    }
}
