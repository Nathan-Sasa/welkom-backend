package com.nathdev.welkom.controllers;

import com.nathdev.welkom.dto.invitation.CreateInvitationRequest;
import com.nathdev.welkom.dto.invitation.InvitationResponse;
import com.nathdev.welkom.repositories.InvitationRepository;
import com.nathdev.welkom.services.InvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Invitation", description = "Endpoints pour créer, modifier et lire les invitations")
public class InvitationController {
    private final InvitationService invitationService;

    @PostMapping("/{eventUuid}/invitations")
    @Operation(summary = "Créer une invitation")
    public ResponseEntity<@NotNull InvitationResponse> create (
            @PathVariable UUID eventUuid,
            @RequestBody CreateInvitationRequest request
            ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(invitationService.create(eventUuid, request));
    }

    @GetMapping("/{eventUuid}/invitations")
    @Operation(summary = "Liste des invitations par l'uuid d'un événement.")
    public ResponseEntity<@NotNull List<InvitationResponse>> getAll (
            @PathVariable UUID eventUuid
    ){
        return ResponseEntity.ok(invitationService.getAllByEvent(eventUuid));
    }

    @GetMapping("/invitations/{uuid}")
    @Operation(summary = "Lire une invitation par son uuid.")
    public ResponseEntity<@NotNull InvitationResponse> get (
            @PathVariable UUID uuid
    ){
        return ResponseEntity.ok(invitationService.findByUuid(uuid));
    }
}
