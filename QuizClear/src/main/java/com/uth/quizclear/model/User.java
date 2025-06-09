package com.uth.quizclear.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    // TODO: ACTIVE
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    // TODO: DEFAULT CURRENT_TIMESTAMP
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    // TODO: DEFAULT NULL
    // ON UPDATE CURRENT_TIMESTAMP
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "department")
    private String department;

    @Column(name = "start")
    private LocalDateTime startDate;

    @Column(name = "end")
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "nation")
    private String nation;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "hometown")
    private String hometown;

    @Column(name = "contact_address")
    private String contactAddress;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // TODO: DEFAULT 0
    @Column(name = "login_attempts")
    private Integer loginAttempts;

    // TODO: DEFAULT FALSE
    @Column(name = "is_locked")
    private Boolean isLocked;
}
