package com.vivek.spring_boot_rest.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Mock AI Service for resume parsing and job matching
 * This can be replaced with actual Spring AI integration when available
 */
@Service
@Slf4j
public class MockAiService {

    private final Random random = new Random();

    /**
     * Mock method to parse resume text and extract information
     */
    public ResumeParsingResult parseResume(String resumeText) {
        log.info("Mock AI: Parsing resume text (length: {})", resumeText.length());
        
        // Simulate AI processing delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Extract basic information from text (simplified parsing)
        String summary = generateMockSummary(resumeText);
        String skills = extractMockSkills(resumeText);
        String experience = extractMockExperience(resumeText);
        String education = extractMockEducation(resumeText);

        return new ResumeParsingResult(summary, skills, experience, education);
    }

    /**
     * Mock method to calculate match score between job and resume
     */
    public MatchResult calculateMatchScore(String jobProfile, String jobDescription, 
                                         String jobTechStack, String candidateSkills, 
                                         String candidateExperience) {
        log.info("Mock AI: Calculating match score for job: {}", jobProfile);
        
        // Simulate AI processing delay
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simple scoring algorithm
        double score = calculateSimpleMatchScore(jobProfile, jobDescription, 
                                               jobTechStack, candidateSkills, candidateExperience);
        String reason = generateMatchReason(score, jobProfile, candidateSkills);

        return new MatchResult(score, reason);
    }

    private String generateMockSummary(String resumeText) {
        // Create a more realistic summary based on actual resume content
        String lowerText = resumeText.toLowerCase();
        
        // Extract key information from the resume
        StringBuilder summary = new StringBuilder();
        
        // Check for experience level
        if (lowerText.contains("senior") || lowerText.contains("lead") || lowerText.contains("manager")) {
            summary.append("Senior-level professional ");
        } else if (lowerText.contains("junior") || lowerText.contains("entry") || lowerText.contains("graduate")) {
            summary.append("Entry-level professional ");
        } else {
            summary.append("Experienced professional ");
        }
        
        // Check for role/position
        if (lowerText.contains("developer") || lowerText.contains("programmer")) {
            summary.append("with software development expertise. ");
        } else if (lowerText.contains("engineer")) {
            summary.append("with engineering background. ");
        } else if (lowerText.contains("analyst")) {
            summary.append("with analytical skills. ");
        } else {
            summary.append("with technical expertise. ");
        }
        
        // Add skills mention
        summary.append("Demonstrates proficiency in various technologies and frameworks. ");
        
        // Add experience mention
        if (lowerText.contains("experience") || lowerText.contains("worked") || lowerText.contains("years")) {
            summary.append("Has practical experience in the field. ");
        }
        
        return summary.toString();
    }

    private String extractMockSkills(String resumeText) {
        String lowerText = resumeText.toLowerCase();
        StringBuilder skills = new StringBuilder();
        
        // Define skill categories and keywords
        String[][] skillCategories = {
            {"Java", "java", "spring", "hibernate", "maven", "gradle"},
            {"JavaScript", "javascript", "js", "react", "angular", "vue", "node", "express"},
            {"Python", "python", "django", "flask", "pandas", "numpy"},
            {"SQL", "sql", "mysql", "postgresql", "oracle", "database"},
            {"HTML", "html", "css", "bootstrap", "tailwind", "frontend"},
            {"Docker", "docker", "kubernetes", "container", "devops"},
            {"Git", "git", "github", "gitlab", "version control"},
            {"AWS", "aws", "azure", "cloud", "aws", "amazon"}
        };
        
        for (String[] category : skillCategories) {
            String skillName = category[0];
            for (int i = 1; i < category.length; i++) {
                if (lowerText.contains(category[i])) {
                    if (skills.length() > 0) skills.append(", ");
                    skills.append(skillName);
                    break; // Only add each skill once
                }
            }
        }
        
        // If no skills found, add some basic ones
        if (skills.length() == 0) {
            skills.append("General programming, Problem solving, Team collaboration");
        }
        
        return skills.toString();
    }

    private String extractMockExperience(String resumeText) {
        String lowerText = resumeText.toLowerCase();
        
        // Look for experience patterns in the text
        if (lowerText.contains("years") || lowerText.contains("experience")) {
            // Extract experience information from the text
            String[] lines = resumeText.split("\n");
            for (String line : lines) {
                String lowerLine = line.toLowerCase();
                if (lowerLine.contains("experience") || lowerLine.contains("worked") || 
                    lowerLine.contains("years") || lowerLine.contains("position")) {
                    // Clean up the line and return it
                    String cleaned = line.trim().replaceAll("\\s+", " ");
                    if (cleaned.length() > 10 && cleaned.length() < 200) {
                        return cleaned;
                    }
                }
            }
        }
        
        // Fallback based on content analysis
        if (lowerText.contains("senior") || lowerText.contains("lead")) {
            return "5+ years of professional experience";
        } else if (lowerText.contains("junior") || lowerText.contains("entry")) {
            return "1-2 years of experience";
        } else {
            return "3+ years of relevant experience";
        }
    }

    private String extractMockEducation(String resumeText) {
        String lowerText = resumeText.toLowerCase();
        
        // Look for education patterns in the text
        if (lowerText.contains("education") || lowerText.contains("degree") || 
            lowerText.contains("university") || lowerText.contains("college")) {
            String[] lines = resumeText.split("\n");
            for (String line : lines) {
                String lowerLine = line.toLowerCase();
                if (lowerLine.contains("education") || lowerLine.contains("degree") || 
                    lowerLine.contains("university") || lowerLine.contains("college") ||
                    lowerLine.contains("bachelor") || lowerLine.contains("master")) {
                    // Clean up the line and return it
                    String cleaned = line.trim().replaceAll("\\s+", " ");
                    if (cleaned.length() > 10 && cleaned.length() < 200) {
                        return cleaned;
                    }
                }
            }
        }
        
        // Fallback based on content analysis
        if (lowerText.contains("master") || lowerText.contains("phd")) {
            return "Advanced degree in relevant field";
        } else if (lowerText.contains("bachelor") || lowerText.contains("degree")) {
            return "Bachelor's degree in Computer Science or related field";
        } else {
            return "Educational background in technology or related field";
        }
    }

    private double calculateSimpleMatchScore(String jobProfile, String jobDescription, 
                                           String jobTechStack, String candidateSkills, 
                                           String candidateExperience) {
        double score = 0.5; // Base score
        
        // Profile match
        if (jobProfile.toLowerCase().contains("developer") && 
            candidateSkills.toLowerCase().contains("java")) {
            score += 0.2;
        }
        
        // Tech stack match
        if (jobTechStack != null && candidateSkills != null) {
            String[] techStack = jobTechStack.toLowerCase().split(",");
            String[] skills = candidateSkills.toLowerCase().split(",");
            
            for (String tech : techStack) {
                for (String skill : skills) {
                    if (skill.trim().contains(tech.trim()) || tech.trim().contains(skill.trim())) {
                        score += 0.1;
                        break;
                    }
                }
            }
        }
        
        // Experience level match
        if (jobDescription.toLowerCase().contains("senior") && 
            candidateExperience.toLowerCase().contains("5+")) {
            score += 0.1;
        }
        
        // Cap score at 1.0
        return Math.min(score, 1.0);
    }

    private String generateMatchReason(double score, String jobProfile, String candidateSkills) {
        if (score >= 0.8) {
            return "Excellent match: Strong skill alignment and relevant experience";
        } else if (score >= 0.6) {
            return "Good match: Candidate has relevant skills and experience";
        } else if (score >= 0.4) {
            return "Moderate match: Some relevant skills but may need additional training";
        } else {
            return "Limited match: Candidate may not be the best fit for this role";
        }
    }

    // Data classes for results
    public static class ResumeParsingResult {
        private final String summary;
        private final String skills;
        private final String experience;
        private final String education;

        public ResumeParsingResult(String summary, String skills, String experience, String education) {
            this.summary = summary;
            this.skills = skills;
            this.experience = experience;
            this.education = education;
        }

        public String getSummary() { return summary; }
        public String getSkills() { return skills; }
        public String getExperience() { return experience; }
        public String getEducation() { return education; }
    }

    public static class MatchResult {
        private final double score;
        private final String reason;

        public MatchResult(double score, String reason) {
            this.score = score;
            this.reason = reason;
        }

        public double getScore() { return score; }
        public String getReason() { return reason; }
    }
} 