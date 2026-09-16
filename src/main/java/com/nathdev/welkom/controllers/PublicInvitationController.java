package com.nathdev.welkom.controllers;

import com.nathdev.welkom.dto.invitation.PublicInvitationResponse;
import com.nathdev.welkom.services.PublicInvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/invitations")
@Tag(name = "Vue public des invitations", description = "Endpoints pour permettre aux invités de voir leurs invitation sans avoir à se connecter. Recquire un accessToken comme variable.")
public class PublicInvitationController {

    private  final PublicInvitationService publicInvitationService;

    @GetMapping("/{accessToken}")
    @Operation(summary = "Lire une invitation.")
    public PublicInvitationResponse getPublicInvitation(
            @PathVariable UUID accessToken
    ) {
        return publicInvitationService.getInvitation(accessToken);
    }

    @GetMapping(
            value = "/{accessToken}/qr",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    @Operation(summary = "Recevoir le QR code d'un invité")
    public ResponseEntity<byte @NotNull []> getQRCode(
            @PathVariable UUID accessToken
    ){
        byte[] qrCode = publicInvitationService.getQRCode(accessToken);

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCode);
    }
}
