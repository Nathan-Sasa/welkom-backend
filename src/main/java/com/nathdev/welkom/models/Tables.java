package com.nathdev.welkom.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "seating_table")
@Data
public class Tables {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @UuidGenerator
    @Column(unique = true, nullable = false)
    private String uuid;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "max_seats")
    private int maxSeats = 8;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToOne(mappedBy = "tables", cascade = CascadeType.ALL)
    private Guest guest;
}
