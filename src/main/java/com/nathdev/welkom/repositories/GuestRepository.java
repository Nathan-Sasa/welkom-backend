package com.nathdev.welkom.repositories;

import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.Guest;
import com.nathdev.welkom.models.Tables;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestRepository extends JpaRepository<@NotNull Guest, @NotNull Long> {
    Optional<Guest> findByUuidAndDeletedAtIsNull(@NotNull UUID uuid);
    List<Guest> findAllByEventAndDeletedAtIsNull(Event event);
    List<Guest> findAllByTablesAndDeletedAtIsNull(Tables table);
}
