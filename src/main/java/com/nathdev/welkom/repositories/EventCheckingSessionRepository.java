package com.nathdev.welkom.repositories;

import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.EventCheckingSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventCheckingSessionRepository extends JpaRepository<@NotNull EventCheckingSession, @NotNull Long> {

    Optional<EventCheckingSession> findByTokenAndRevokedAtIsNull(@NotNull UUID token);

    List<EventCheckingSession> findAllByEventAndRevokedAtIsNull(@NotNull Event event);
}
