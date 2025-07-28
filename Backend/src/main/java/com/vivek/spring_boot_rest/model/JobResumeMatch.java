package com.vivek.spring_boot_rest.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_resume_matches")
public class JobResumeMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private JobPost job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(nullable = false)
    private Double matchScore;

    @Column(columnDefinition = "TEXT")
    private String matchReason;

    @Column(nullable = false)
    private LocalDateTime matchedAt;

    @PrePersist
    protected void onCreate() {
        matchedAt = LocalDateTime.now();
    }
} 