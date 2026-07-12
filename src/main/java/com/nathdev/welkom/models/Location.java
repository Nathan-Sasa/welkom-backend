package com.nathdev.welkom.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "event_locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String address;

    private int lat;
    private int lon;

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    private Event event;

}
