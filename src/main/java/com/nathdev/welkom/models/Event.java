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

    @Column(nullable = false)
    private String date_event;

    private Payment_status  payment_status;
    private EventsStatus status;


    private String image;
    private LocalDateTime date_event_start;
    private LocalDateTime date_event_end;

    @Column(name = "estimated_guests", nullable = false)
    private long estimated_guests;

    @Column(name = "security_access_key", nullable = false)
    private String event_key;


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
