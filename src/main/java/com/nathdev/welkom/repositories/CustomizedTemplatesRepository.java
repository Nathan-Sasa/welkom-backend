package com.nathdev.welkom.repositories;

import com.nathdev.welkom.models.CustomizedTemplates;
import com.nathdev.welkom.models.Event;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomizedTemplatesRepository extends JpaRepository <@NotNull CustomizedTemplates, @NotNull Long> {
    Optional <CustomizedTemplates> findByUuid(@NotNull UUID uuid);
    Optional <CustomizedTemplates> findByEvent(@NotNull Event event);
}
