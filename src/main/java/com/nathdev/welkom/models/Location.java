package com.nathdev.welkom.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "event_locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(
            nullable = false,
            precision = 10,
            scale = 7
    )
    private BigDecimal latitude;

    @Column(
            nullable = false,
            precision = 10,
            scale = 7
    )
    private BigDecimal longitude;

    @OneToOne
    @JoinColumn(
            name = "event_id",
            nullable = false

    )
    private Event event;

}
