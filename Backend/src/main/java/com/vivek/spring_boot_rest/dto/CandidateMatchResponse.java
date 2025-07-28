package com.vivek.spring_boot_rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateMatchResponse {
    private Long resumeId;
    private String candidateName;
    private String candidateEmail;
    private String aiSummary;
    private String extractedSkills;
    private Double matchScore;
    private String matchReason;
    private String resumeFileName;
} 