package com.nathdev.welkom.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "guest")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    @Column(unique = true, nullable = false)
    private UUID uuid;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name",  nullable = false, length = 50)
    private String lastName;

    private String email;

    @Column(name = "telephone")
    private String telephone;

    private String category;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    
    @OneToOne
    @JoinColumn(name = "tables_id")
    private Tables tables;

    @OneToOne(mappedBy = "guest", cascade = CascadeType.ALL)
    private Invitation invitation;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
