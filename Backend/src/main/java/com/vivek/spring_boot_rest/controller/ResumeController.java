package com.vivek.spring_boot_rest.controller;

import com.vivek.spring_boot_rest.dto.CandidateMatchResponse;
import com.vivek.spring_boot_rest.dto.ResumeUploadResponse;
import com.vivek.spring_boot_rest.model.JobPost;
import com.vivek.spring_boot_rest.model.Resume;
import com.vivek.spring_boot_rest.model.User;
import com.vivek.spring_boot_rest.service.JobMatchingService;
import com.vivek.spring_boot_rest.service.JobService;
import com.vivek.spring_boot_rest.service.ResumeParsingService;
import com.vivek.spring_boot_rest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@CrossOrigin(origins = {"http://localhost", "http://localhost:80", "http://frontend:80"}, allowCredentials = "true")
@Slf4j
public class ResumeController {

    @Autowired
    private ResumeParsingService resumeParsingService;

    @Autowired
    private JobMatchingService jobMatchingService;

    @Autowired
    private JobService jobService;

    @Autowired
    private UserService userService;

    /**
     * Upload and parse a resume using AI
     * POST /api/resumes/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<ResumeUploadResponse> uploadResume(@RequestParam("file") MultipartFile file) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username);

            if (user == null) {
                ResumeUploadResponse errorResponse = new ResumeUploadResponse();
                errorResponse.setMessage("User not found");
                errorResponse.setSuccess(false);
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Validate file
            if (file.isEmpty()) {
                ResumeUploadResponse errorResponse = new ResumeUploadResponse();
                errorResponse.setMessage("Please select a file to upload");
                errorResponse.setSuccess(false);
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Check file type
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("application/pdf") && 
                !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
                ResumeUploadResponse errorResponse = new ResumeUploadResponse();
                errorResponse.setMessage("Only PDF and DOCX files are supported");
                errorResponse.setSuccess(false);
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Parse and save resume
            ResumeUploadResponse response = resumeParsingService.parseAndSaveResume(file, user);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            log.error("Error uploading resume: ", e);
            ResumeUploadResponse errorResponse = new ResumeUploadResponse();
            errorResponse.setMessage("Error processing resume: " + e.getMessage());
            errorResponse.setSuccess(false);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get ranked candidates for a specific job
     * GET /api/jobs/{jobId}/candidates
     */
    @GetMapping("/jobs/{jobId}/candidates")
    public ResponseEntity<List<CandidateMatchResponse>> getCandidatesForJob(@PathVariable Integer jobId) {
        try {
            // Get the job
            JobPost job = jobService.getJob(jobId);
            if (job == null || job.getPostId() == 0) {
                return ResponseEntity.notFound().build();
            }

            // Get ranked candidates
            List<CandidateMatchResponse> candidates = jobMatchingService.getRankedCandidatesForJob(jobId, job);
            
            return ResponseEntity.ok(candidates);

        } catch (Exception e) {
            log.error("Error getting candidates for job {}: ", jobId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get current user's resume summary
     * GET /api/resumes/my-summary
     */
    @GetMapping("/my-summary")
    public ResponseEntity<ResumeUploadResponse> getMyResumeSummary() {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username);

            if (user == null) {
                ResumeUploadResponse errorResponse = new ResumeUploadResponse();
                errorResponse.setMessage("User not found");
                errorResponse.setSuccess(false);
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Get user's latest resume
            Resume latestResume = resumeParsingService.getLatestResumeByUser(user);
            
            if (latestResume == null) {
                ResumeUploadResponse noResumeResponse = new ResumeUploadResponse();
                noResumeResponse.setMessage("No resume uploaded yet");
                noResumeResponse.setSuccess(false);
                return ResponseEntity.ok().body(noResumeResponse);
            }

            // Return resume summary
            ResumeUploadResponse response = new ResumeUploadResponse();
            response.setResumeId(latestResume.getId());
            response.setFileName(latestResume.getFileName());
            response.setAiSummary(latestResume.getAiSummary());
            response.setExtractedSkills(latestResume.getExtractedSkills());
            response.setExperience(latestResume.getExperience());
            response.setEducation(latestResume.getEducation());
            response.setParsedText(latestResume.getParsedText());
            response.setMessage("Resume summary retrieved successfully");
            response.setSuccess(true);
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error getting resume summary: ", e);
            ResumeUploadResponse errorResponse = new ResumeUploadResponse();
            errorResponse.setMessage("Error retrieving resume summary: " + e.getMessage());
            errorResponse.setSuccess(false);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
} 