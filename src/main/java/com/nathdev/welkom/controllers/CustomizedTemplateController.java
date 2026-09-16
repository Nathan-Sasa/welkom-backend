package com.nathdev.welkom.controllers;

import com.nathdev.welkom.dto.customizedTemplate.CustomizeTemplateRequest;
import com.nathdev.welkom.dto.customizedTemplate.CustomizeTemplateResponse;
import com.nathdev.welkom.services.CustomizedTemplatesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/events")
@Tag(name = "Custom catalogue", description = "Endpoint pour Copier la catalogue, modifier la catalogue customizé par événement.")
@RequiredArgsConstructor
public class CustomizedTemplateController {

    private final CustomizedTemplatesService customizedTemplatesService;

    @PostMapping("/{eventUuid}/customized-template")
    @Operation(summary = "Copie d'une catalogue")
    public ResponseEntity<@NotNull CustomizeTemplateResponse> createCustomizedTemplate(
            @PathVariable UUID eventUuid,
            @RequestBody CustomizeTemplateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customizedTemplatesService.create(eventUuid, request));
    }

    @GetMapping("/{eventUuid}/customized-template")
    @Operation(summary = "Voir la catalogue customizé d'un événement")
    public ResponseEntity<@NotNull CustomizeTemplateResponse> getCustomizedTemplate(
            @PathVariable UUID eventUuid
    ) {
        return ResponseEntity
                .ok(customizedTemplatesService.getByEvent(eventUuid));
    }

    @PatchMapping("/{eventUuid}/customized-template")
    @Operation(summary = "Modifier la catalogue customizé d'un événement")
    public ResponseEntity<@NotNull CustomizeTemplateResponse> updateCustomizedTemplate(
            @PathVariable UUID eventUuid,
            @RequestBody CustomizeTemplateRequest request
    ) {
        return ResponseEntity.ok(customizedTemplatesService.updateCustomizedTempale(eventUuid, request));
    }

}
