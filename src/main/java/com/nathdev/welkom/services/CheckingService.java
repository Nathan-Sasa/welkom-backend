package com.nathdev.welkom.services;

import com.nathdev.welkom.enums.PaymentStatus;
import com.nathdev.welkom.exceptions.accessDenied.AccessDeniedCustomException;
import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.EventCheckingSession;
import com.nathdev.welkom.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckingService {

    private final EventRepository eventRepository;
    private final EventCheckingSessionService eventCheckingSessionService;

    // =====================================================================================
    // Security checking event
    // =====================================================================================

    public EventCheckingSession accessChecking(String securityEventKey) {
        Event event = eventRepository
                .findBySecurityEventKeyAndDeletedAtIsNull(securityEventKey)
                .orElseThrow(() -> new AccessDeniedCustomException("Accès session réfusé !"));

        if (event.getPaymentStatus() != PaymentStatus.PAYMENT_SUCCESS) {
            throw new AccessDeniedCustomException("Accès réfusé ! Veillez activer l'événement pour acceder à l'espace checking.");
        }

        return eventCheckingSessionService.createSession(event);
    }
}