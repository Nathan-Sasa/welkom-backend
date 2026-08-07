package com.nathdev.welkom.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "event_locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String address;

    private int lat;
    private int lon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

}
