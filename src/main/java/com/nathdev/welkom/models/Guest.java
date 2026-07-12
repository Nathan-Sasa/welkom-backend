package com.nathdev.welkom.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Guest {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private String first_name;

    @Column(nullable = false)
    private String last_name;

    private String email;

    @Column(nullable = false)
    private String telephone;

    private String category;
    private String table;

    @OneToOne(mappedBy = "guest", cascade = CascadeType.ALL)
    private Invitation invitation;
}
