package com.nathdev.welkom.controllers;

import com.nathdev.welkom.dto.customizedTemplate.CustomizeTemplateRequest;
import com.nathdev.welkom.dto.customizedTemplate.CustomizeTemplateResponse;
import com.nathdev.welkom.services.CustomizedTemplatesService;
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
@RequiredArgsConstructor
public class CustomizedTemplateController {

    private final CustomizedTemplatesService customizedTemplatesService;

    @PostMapping("/{eventUuid}/customized-template")
    public ResponseEntity<@NotNull CustomizeTemplateResponse> createCustomizedTemplate(
            @PathVariable UUID eventUuid,
            @RequestBody CustomizeTemplateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customizedTemplatesService.create(eventUuid, request));
    }

    @GetMapping("/{eventUuid}/customized-template")
    public ResponseEntity<@NotNull CustomizeTemplateResponse> getCustomizedTemplate(
            @PathVariable UUID eventUuid
    ) {
        return ResponseEntity
                .ok(customizedTemplatesService.getByEvent(eventUuid));
    }

    @PatchMapping("/{eventUuid}/customized-template")
    public ResponseEntity<@NotNull CustomizeTemplateResponse> updateCustomizedTemplate(
            @PathVariable UUID eventUuid,
            @RequestBody CustomizeTemplateRequest request
    ) {
        return ResponseEntity.ok(customizedTemplatesService.updateCustomizedTempale(eventUuid, request));
    }

}
