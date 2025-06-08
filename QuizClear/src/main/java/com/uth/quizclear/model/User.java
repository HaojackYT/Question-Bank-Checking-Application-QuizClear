package com.uth.quizclear.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
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
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private String department;

    @Column(name = "start")
    private LocalDateTime startDate; 

    @Column(name = "end")
    private LocalDateTime endDate; 

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String nation;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String hometown;

    @Column(name = "contact_address")
    private String contactAddress;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "last_login")
    private LocalDateTime lastLogin; 

    @Column(name = "login_attempts")
    private Integer loginAttempts; 

    @Column(name = "is_locked")
    private Boolean isLocked;

    // Relationships
    // @OneToMany(mappedBy = "createdBy")
    // private Set<Course> coursesCreated;

    // @OneToMany(mappedBy = "assignedTo")
    // private Set<Plan> plansAssigned;

    // @OneToMany(mappedBy = "assignedBy")
    // private Set<Plan> plansAssignedBy; 

    // @OneToMany(mappedBy = "assignedTo")
    // private Set<Task> tasksAssigned;

    // @OneToMany(mappedBy = "assignedBy")
    // private Set<Task> tasksAssignedBy;

    // @OneToMany(mappedBy = "createdBy")
    // private Set<Question> questionsCreated;

    // @OneToMany(mappedBy = "reviewedBy")
    // private Set<Question> questionsReviewed; 

    // @OneToMany(mappedBy = "approvedBy")
    // private Set<Question> questionsApproved; 

    // @OneToMany(mappedBy = "createdBy")
    // private Set<Exam> examsCreated;

    // @OneToMany(mappedBy = "reviewedBy")
    // private Set<Exam> examsReviewed; 

    // @OneToMany(mappedBy = "approvedBy")
    // private Set<Exam> examsApproved; 

    // @OneToMany(mappedBy = "checkedBy")
    // private Set<AI_DuplicateCheck> aiDuplicateChecks;

    // @OneToMany(mappedBy = "detectedBy")
    // private Set<DuplicateDetection> detectionsDetected; 

    // @OneToMany(mappedBy = "processedBy")
    // private Set<DuplicateDetection> detectionsProcessed;

    // @OneToMany(mappedBy = "commenter")
    // private Set<Comment> comments; 

    // @OneToMany(mappedBy = "user")
    // private Set<Notification> notifications;

    // @OneToMany(mappedBy = "user")
    // private Set<ActivityLog> activityLogs;

    // @OneToMany(mappedBy = "reviewer")
    // private Set<ExamReview> examReviews;

    // @OneToMany(mappedBy = "uploadedBy")
    // private Set<QuestionAttachment> questionAttachments; 

    // @OneToMany(mappedBy = "updatedBy")
    // private Set<SystemConfig> systemConfigs;

    // @OneToMany(mappedBy = "user")
    // private Set<UserSession> userSessions;

    // @OneToMany(mappedBy = "user")
    // private Set<DashboardStat> dashboardStats;

    // @OneToMany(mappedBy = "exportedBy")
    // private Set<ExamExport> examExports;
}
