package com.nathdev.welkom.repositories;

import com.nathdev.welkom.models.Template;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TemplateRepository extends JpaRepository<@NotNull Template, @NotNull Long> {
    Optional<Template> findByUuid(@NotNull UUID uuid);
}
