package com.nathdev.welkom.repositories;

import com.nathdev.welkom.models.Location;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<@NotNull Location, @NotNull Long> {
    Optional<@NotNull Location> findByEvent(@NotNull UUID eventUuid);
}
