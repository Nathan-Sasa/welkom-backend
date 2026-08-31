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

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class InvitationController {
    private final InvitationRepository invitationRepository;
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

    @GetMapping("/{eventUuid}/invitations/{id}")
    public ResponseEntity<@NotNull Optional<Object>> getInvitation(
            @PathVariable UUID id
    ){
        try{
            Optional<Object> invitation = Optional.ofNullable(invitationService.findInvitation(id));
            return ResponseEntity.status(HttpStatus.OK).body(invitation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
