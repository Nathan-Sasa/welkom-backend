package com.nathdev.welkom.models;

import com.nathdev.welkom.enums.Payment_status;
import com.nathdev.welkom.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String date_event;

    @OneToMany(mappedBy = "event")
    private List<Guest> guests;

    private Status status;
    private Payment_status  payment_status;
}
