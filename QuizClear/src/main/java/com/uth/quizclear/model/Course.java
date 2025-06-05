package com.uth.quizclear.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    private String courseCode;
    private String courseName;
    private Integer credits;
    private String department;
    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    private Date createdAt;
    private String courseNote;
}
