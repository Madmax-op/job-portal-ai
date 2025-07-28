package com.vivek.spring_boot_rest.service;

import com.vivek.spring_boot_rest.dto.ResumeUploadResponse;
import com.vivek.spring_boot_rest.model.Resume;
import com.vivek.spring_boot_rest.model.User;
import com.vivek.spring_boot_rest.repo.ResumeRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import com.vivek.spring_boot_rest.service.MockAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ResumeParsingService {

    @Autowired
    private ResumeRepo resumeRepo;

    @Autowired
    private MockAiService mockAiService;

    private final Tika tika = new Tika();
    private static final String UPLOAD_DIR = "uploads/resumes/";

    public ResumeUploadResponse parseAndSaveResume(MultipartFile file, User user) {
        try {
            // Create upload directory if it doesn't exist
            createUploadDirectory();

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            String filePath = UPLOAD_DIR + uniqueFilename;

            // Save file to disk
            Path path = Paths.get(filePath);
            Files.copy(file.getInputStream(), path);

            // Extract text from document
            String extractedText = extractTextFromFile(path.toFile());

            // Parse resume using AI
            MockAiService.ResumeParsingResult parsingResult = mockAiService.parseResume(extractedText);

            // Save to database
            Resume resume = new Resume();
            resume.setUser(user);
            resume.setFileName(originalFilename);
            resume.setFilePath(filePath);
            resume.setParsedText(extractedText);
            resume.setAiSummary(parsingResult.getSummary());
            resume.setExtractedSkills(parsingResult.getSkills());
            resume.setExperience(parsingResult.getExperience());
            resume.setEducation(parsingResult.getEducation());

            Resume savedResume = resumeRepo.save(resume);

            ResumeUploadResponse response = new ResumeUploadResponse();
            response.setResumeId(savedResume.getId());
            response.setFileName(originalFilename);
            response.setAiSummary(parsingResult.getSummary());
            response.setExtractedSkills(parsingResult.getSkills());
            response.setExperience(parsingResult.getExperience());
            response.setEducation(parsingResult.getEducation());
            response.setParsedText(extractedText);
            response.setMessage("Resume parsed and saved successfully");
            response.setSuccess(true);
            return response;

        } catch (Exception e) {
            log.error("Error parsing resume: ", e);
            ResumeUploadResponse errorResponse = new ResumeUploadResponse();
            errorResponse.setMessage("Error parsing resume: " + e.getMessage());
            errorResponse.setSuccess(false);
            return errorResponse;
        }
    }

    private void createUploadDirectory() throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    private String extractTextFromFile(File file) throws IOException, TikaException {
        return tika.parseToString(file);
    }

    /**
     * Get the latest resume uploaded by a user
     */
    public Resume getLatestResumeByUser(User user) {
        List<Resume> resumes = resumeRepo.findTopByUserOrderByUploadedAtDesc(user);
        return resumes.isEmpty() ? null : resumes.get(0);
    }
} 