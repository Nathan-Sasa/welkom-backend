package com.nathdev.welkom.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(length = 30)
    private String first_name;

    private String email;

    @Column(length = 30)
    private String telephone;
    private String avatar;
    private String avatar_public;

    private LocalDateTime lastLogin;
}
