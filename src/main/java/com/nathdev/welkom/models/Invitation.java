package com.nathdev.welkom.models;

import com.nathdev.welkom.enums.Rsvp_status;
import com.nathdev.welkom.enums.ScanStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Data
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @UuidGenerator
    @Column(unique = true, nullable = false)
    private String uuid;

    @Column(name = "rsvp_status", length = 20)
    private Rsvp_status rsvpStatus = Rsvp_status.PENDING;

    @Column(name = "scan_status", length = 20)
    private ScanStatus scanStatus = ScanStatus.PENDING;

    @Column(name="scanned_at")
    private LocalDateTime scannedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToOne
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @OneToOne
    @JoinColumn(name = "customized_template_id", nullable = false)
    private Template customizedTemplate;
}
