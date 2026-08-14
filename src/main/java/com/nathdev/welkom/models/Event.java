package com.nathdev.welkom.models;

import com.nathdev.welkom.enums.EventsStatus;
import com.nathdev.welkom.enums.Payment_status;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @UuidGenerator
    @Column(unique = true, nullable = false)
    private String uuid;

    @Column(name = "secure_id", unique = true,  nullable = false)
    private String secureId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "date_event",nullable = false)
    private String dateEvent;

    @Column(name = "payment_status")
    private Payment_status  paymentStatus;

    private EventsStatus status;

    private String image;

    @Column(name = "date_event_start")
    private LocalDateTime dateEventStart;

    @Column(name = "date_event_end")
    private LocalDateTime dateEventEnd;

    @Column(name = "estimated_guests", nullable = false)
    private long estimatedGuests;

    @Column(name = "security_access_key", nullable = false)
    private String securityEventKey;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Location> location;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Guest> guests;

    @OneToMany(mappedBy = "event", cascade =  CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tables> tables;

    @OneToOne(mappedBy = "event", cascade =  CascadeType.ALL)
    private CustomizedTemplates customizedTemplates;

    @OneToMany(mappedBy = "event", cascade =  CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Invitation> invitations;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
