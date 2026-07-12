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

    private String username;
    private String email;
    private String telephone;
    private String avatar;
    private String avatar_public;
    private LocalDateTime lastLogin;
}
