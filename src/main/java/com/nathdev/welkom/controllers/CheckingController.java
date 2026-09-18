package com.nathdev.welkom.controllers;

import com.nathdev.welkom.dto.checking.CheckingAccessRequest;
import com.nathdev.welkom.models.EventCheckingSession;
import com.nathdev.welkom.services.CheckingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/checking")
@Tag( name = "Checking",description = "Endpoint du checking des événements")
public class CheckingController {
    private final CheckingService checkingService;

    @PostMapping("/access")
    @Operation(summary = "Authentification de l'événement pour l'espace checking.")
    public ResponseEntity<@NotNull Void> accessChecking(
            @RequestBody CheckingAccessRequest request,
            HttpServletResponse response
    ){
        EventCheckingSession session =
                checkingService.accessChecking(request.securityEventKey());

        ResponseCookie cookie = ResponseCookie
                .from("welkom_event_access", session.getToken().toString())
                .httpOnly(true)
                .secure(false) //localhost uniquement
                .sameSite("Strict")
                .path("/api/v1/checking")
                .maxAge(Duration.ofHours(2))
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );

        return ResponseEntity.noContent().build();
    }
}
