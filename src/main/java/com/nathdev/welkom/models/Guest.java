package com.nathdev.welkom.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "guest")
public class Guest {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String first_name;

    @Column(nullable = false)
    private String last_name;

    private String email;

    @Column(nullable = false)
    private String telephone;

    private String category;
    private String table;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    
    @OneToOne
    @JoinColumn(name = "table_id")
    private Tables tables;

    @OneToOne(mappedBy = "guest", cascade = CascadeType.ALL)
    private Invitation invitation;

}
