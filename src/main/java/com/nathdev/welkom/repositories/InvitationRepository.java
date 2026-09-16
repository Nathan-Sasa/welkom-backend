package com.nathdev.welkom.repositories;

import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.Guest;
import com.nathdev.welkom.models.Invitation;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<@NotNull Invitation, @NotNull Long> {
    Optional<Invitation> findByUuid(UUID uuid);
    Optional<Invitation> findByGuest(Guest guest);
    Optional<Invitation> findByAccessToken(UUID accessToken);
    Optional<Invitation> findByEvent(Event event);
    List<Invitation> findAllByEvent(Event event);
    Optional<Invitation> findByQrToken(UUID qrToken);
}
