package com.nathdev.welkom.repositories;

import com.nathdev.welkom.models.Event;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<@NotNull Event, @NotNull Long> {

    Optional<Event> findBySecureId(String secureId);
    Optional<Event> findByUuid(UUID uuid);
}
