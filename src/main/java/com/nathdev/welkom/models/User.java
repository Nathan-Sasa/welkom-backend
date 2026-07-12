package com.nathdev.welkom.models;

import com.nathdev.welkom.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Column(nullable = false)
    private String role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//    private LocalDateTime lastLogin;
    private String activated;
    private UserStatus status;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Event> events;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now();
        if (status == null) {
            status = UserStatus.ACTIVE;
        }
    }
    @PreUpdate
    protected void onUpdate() {updatedAt = LocalDateTime.now();}

}
