package com.nathdev.welkom.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @GeneratedValue
    @UuidGenerator
    @Column(unique = true, nullable = false)
    private String uuid;

    @OneToOne
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    private String rsvp;

}
