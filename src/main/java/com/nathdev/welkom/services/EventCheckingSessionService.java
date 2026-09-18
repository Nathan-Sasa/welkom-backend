package com.nathdev.welkom.services;

import com.nathdev.welkom.enums.PaymentStatus;
import com.nathdev.welkom.exceptions.accessDenied.AccessDeniedCustomException;
import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.EventCheckingSession;
import com.nathdev.welkom.repositories.EventCheckingSessionRepository;
import com.nathdev.welkom.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventCheckingSessionService {
    private final EventCheckingSessionRepository sessionRepository;
    private final EventRepository eventRepository;

    public EventCheckingSession createSession(Event event) {
        EventCheckingSession session = new EventCheckingSession();

        session.setToken(UUID.randomUUID());
        session.setEvent(event);
        session.setExpiresAt(LocalDateTime.now().plusHours(2));

        return sessionRepository.save(session);
    }

    public EventCheckingSession getValidSession(UUID token){
        EventCheckingSession session = sessionRepository
                .findByTokenAndRevokedAtIsNull(token)
                .orElseThrow(() -> new AccessDeniedCustomException("Accès session réfusé"));

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AccessDeniedCustomException("Accès session expiré");
        }

        return session;
    }

    public void revokeActiveSessions(Event event){
        List<EventCheckingSession> sessions =
                sessionRepository.findAllByEventAndRevokedAtIsNull(event);

        LocalDateTime now = LocalDateTime.now();

        sessions.forEach(session -> session.setExpiresAt(now));
        sessionRepository.saveAll(sessions);
    }
}
