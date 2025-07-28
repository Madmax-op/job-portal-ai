package com.vivek.spring_boot_rest.service;

import com.vivek.spring_boot_rest.dto.CandidateMatchResponse;
import com.vivek.spring_boot_rest.model.JobPost;
import com.vivek.spring_boot_rest.model.JobResumeMatch;
import com.vivek.spring_boot_rest.model.Resume;
import com.vivek.spring_boot_rest.repo.JobResumeMatchRepo;
import com.vivek.spring_boot_rest.repo.ResumeRepo;
import lombok.extern.slf4j.Slf4j;
import com.vivek.spring_boot_rest.service.MockAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JobMatchingService {

    @Autowired
    private ResumeRepo resumeRepo;

    @Autowired
    private JobResumeMatchRepo jobResumeMatchRepo;

    @Autowired
    private MockAiService mockAiService;

    public List<CandidateMatchResponse> getRankedCandidatesForJob(Integer jobId, JobPost job) {
        try {
            // Get all resumes
            List<Resume> allResumes = resumeRepo.findAll();
            
            // Calculate match scores for each resume
            List<JobResumeMatch> matches = allResumes.stream()
                .map(resume -> calculateMatchScore(job, resume))
                .collect(Collectors.toList());

            // Save or update matches in database
            matches.forEach(match -> {
                jobResumeMatchRepo.findByJobPostIdAndResumeId(jobId, match.getResume().getId())
                    .ifPresentOrElse(
                        existingMatch -> {
                            existingMatch.setMatchScore(match.getMatchScore());
                            existingMatch.setMatchReason(match.getMatchReason());
                            jobResumeMatchRepo.save(existingMatch);
                        },
                        () -> jobResumeMatchRepo.save(match)
                    );
            });

            // Return ranked candidates
            return matches.stream()
                .sorted((m1, m2) -> Double.compare(m2.getMatchScore(), m1.getMatchScore()))
                .map(this::convertToCandidateResponse)
                .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error getting ranked candidates for job {}: ", jobId, e);
            return List.of();
        }
    }

    private JobResumeMatch calculateMatchScore(JobPost job, Resume resume) {
        try {
            String jobTechStack = job.getPostTechStack() != null ? String.join(", ", job.getPostTechStack()) : "Not specified";
            
            MockAiService.MatchResult matchResult = mockAiService.calculateMatchScore(
                job.getPostProfile(),
                job.getPostDesc(),
                jobTechStack,
                resume.getExtractedSkills(),
                resume.getExperience()
            );
            
            JobResumeMatch match = new JobResumeMatch();
            match.setJob(job);
            match.setResume(resume);
            match.setMatchScore(matchResult.getScore());
            match.setMatchReason(matchResult.getReason());
            
            return match;

        } catch (Exception e) {
            log.error("Error calculating match score: ", e);
            // Fallback to basic scoring
            return createFallbackMatch(job, resume);
        }
    }

    private JobResumeMatch createFallbackMatch(JobPost job, Resume resume) {
        JobResumeMatch match = new JobResumeMatch();
        match.setJob(job);
        match.setResume(resume);
        match.setMatchScore(0.5);
        match.setMatchReason("Basic match analysis completed");
        return match;
    }

    private CandidateMatchResponse convertToCandidateResponse(JobResumeMatch match) {
        Resume resume = match.getResume();
        return new CandidateMatchResponse(
            resume.getId(),
            resume.getUser().getUsername(),
            resume.getUser().getEmail(),
            resume.getAiSummary(),
            resume.getExtractedSkills(),
            match.getMatchScore(),
            match.getMatchReason(),
            resume.getFileName()
        );
    }
} 