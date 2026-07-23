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
    private int id;

    @UuidGenerator
    @Column(unique = true, nullable = false)
    private String uuid;

    @OneToOne
    @JoinColumn(name = "guest_id", nullable = false, unique = true)
    private Guest guest;

    @ManyToOne
    @JoinColumn(name = "customized_template_id", nullable = false)
    private Template customizedTemplate;

    @Column(name = "rsvp_status", length = 20)
    private Rsvp_status rsvpStatus = Rsvp_status.PENDING;

    @Column(name = "scan_status", length = 20)
    private ScanStatus scanStatus = ScanStatus.PENDING;

    @Column(name="scanned_at")
    private LocalDateTime scannedAt;
}
