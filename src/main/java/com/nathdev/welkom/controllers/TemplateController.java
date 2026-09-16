package com.nathdev.welkom.controllers;

import com.nathdev.welkom.dto.template.CreateTemplateRequest;
import com.nathdev.welkom.dto.template.TemplateResponse;
import com.nathdev.welkom.dto.template.UpdateTemplateRequest;
import com.nathdev.welkom.models.Template;
import com.nathdev.welkom.security.JwtUtils;
import com.nathdev.welkom.services.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/templates")
@Tag(name = "Catalogue", description = "Endpoint pour créer, modifier, supprimer et lire les details d'une catalogue")
@RequiredArgsConstructor
public class TemplateController {
    private final TemplateService templateService;

    @GetMapping("/list")
    @Operation(summary = "List des catalogues")
    public ResponseEntity<@NotNull List<TemplateResponse>> getAllTemplates() {
        return ResponseEntity.ok(
                templateService.getTemplates()
        );
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Details d'une catalogue")
    public ResponseEntity<@NotNull TemplateResponse> getTemplateById(@PathVariable UUID uuid) {
        if (uuid == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(templateService.getTemplateById(uuid));
    }

    @PostMapping("/create")
    @Operation(summary = "créer ")
    public ResponseEntity<@NotNull TemplateResponse> createInvitation(@RequestBody CreateTemplateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(templateService.createTemplate(request));
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<@NotNull TemplateResponse> updateTemplate(
            @PathVariable UUID uuid,
            @RequestBody UpdateTemplateRequest request
            ) {
        return ResponseEntity.ok(templateService.updateTemplate(uuid, request));
    }


    @DeleteMapping("/{uuid}")
    public ResponseEntity<@NotNull Void> deleteTemplate(
            @PathVariable UUID uuid
    ) {
        templateService.deleteTemplate(uuid);
        return ResponseEntity.noContent().build();
    }

}
