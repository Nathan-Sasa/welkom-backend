package com.nathdev.welkom.controllers;

import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.User;
import com.nathdev.welkom.services.EventService;
import com.nathdev.welkom.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(HttpServletRequest request,@RequestBody Event event) {
        try{

            if (request != null) {
                User user = userService.getUserAuth(request);
                if (user != null && Objects.equals(user.getRole(), "WLK_USER")) {
                    eventService.createEvent(user, event);
                    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Evénement créé avec succès"));
                }
                else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Utilisateur non autorisé"));
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Utilisateur introuvable :: " ));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }
}
