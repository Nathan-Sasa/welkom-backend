package com.nathdev.welkom.repositories;

import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.Tables;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TableRepository extends JpaRepository<@NotNull Tables, @NotNull Long> {
    Optional<Tables> findByUuid(UUID uuid);
    Optional<Tables> findByTableName(String tableName);
    List<Tables> findAllByEvent(Event event);
}
