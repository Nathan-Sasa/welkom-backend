package com.nathdev.welkom.repositories;

import com.nathdev.welkom.models.Invitation;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<@NotNull Invitation, @NotNull Long> {
    Optional<Invitation> findByUuid(UUID uuid);
}
