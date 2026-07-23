package com.nathdev.welkom.models;

import com.nathdev.welkom.enums.Payment_status;
import com.nathdev.welkom.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String date_event;

    private Status status;
    private Payment_status  payment_status = Payment_status.PENDING;

    private LocalDateTime date_event_start;
    private LocalDateTime date_event_end;

    @Column(name = "estimated_guests", nullable = false)
    private int estimated_guests;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Guest> guests;

    @OneToMany(mappedBy = "event", cascade =  CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tables> tables;

    @OneToOne(mappedBy = "event", cascade =  CascadeType.ALL, fetch = FetchType.LAZY)
    private CustomizerTemplates customizerTemplates;


    @PrePersist
    private void onCreate(){
        date_event_start = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate(){
        date_event_end = LocalDateTime.now();
    }
}
