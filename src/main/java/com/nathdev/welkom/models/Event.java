package com.nathdev.welkom.models;

import com.nathdev.welkom.enums.EventsStatus;
import com.nathdev.welkom.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    @Column(
            unique = true,
            nullable = false
    )
    private UUID uuid;

    @Column(
            name = "secure_id",
            unique = true,
            nullable = false
    )
    private String secureId;

    @Column(
            name = "security_access_key",
            nullable = false,
            unique = true
    )
    private String securityEventKey;

    @Column(
            nullable = false
    )
    private String title;

    private String description;

    @Column(
            name = "date_event_start",
            nullable = false
    )
    private LocalDateTime dateEventStart;

    @Column(
            name = "date_event_end",
            nullable = false
    )
    private LocalDateTime dateEventEnd;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "payment_status"
    )
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private EventsStatus status;

    private String image;

    @Column(name = "estimated_guests", nullable = false)
    private Integer estimatedGuests;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @OneToOne(
            mappedBy = "event",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Location location;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<Guest> guests;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<Tables> tables;

    @OneToOne(
            mappedBy = "event",
            cascade =  CascadeType.ALL,
            orphanRemoval = true
    )
    private CustomizedTemplates customizedTemplate;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<Invitation> invitations;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @PrePersist
    private void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate(){

        updatedAt = LocalDateTime.now();
    }
}
