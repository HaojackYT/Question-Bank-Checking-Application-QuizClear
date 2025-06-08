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
@AllArgsConstructor // Quan trọng để Lombok tạo constructor với tất cả các trường
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
    private UserRole role; // Sử dụng enum UserRole

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status; // Sử dụng enum UserStatus

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Nullable, tự động cập nhật bởi DB

    @Column(name = "department")
    private String department;

    @Column(name = "start_date") // Đổi tên cột cho rõ ràng hơn (nếu trong DB là 'start')
    private LocalDateTime startDate;

    @Column(name = "end_date") // Đổi tên cột cho rõ ràng hơn (nếu trong DB là 'end')
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender; // Sử dụng enum Gender

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
}