package com.uth.quizclear.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String fullName;
    private String email;
    private String passwordHash;
    private String role;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String department;
    private Date start;
    private Date end;
    private String gender;
    private Date dateOfBirth;
    private String nation;
    private String phoneNumber;
    private String hometown;
    private String contactAddress;
}
