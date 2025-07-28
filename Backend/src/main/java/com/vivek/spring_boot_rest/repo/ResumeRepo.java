package com.vivek.spring_boot_rest.repo;

import com.vivek.spring_boot_rest.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepo extends JpaRepository<Resume, Long> {
    
    List<Resume> findByUserEmail(String userEmail);
    
    @Query("SELECT r FROM Resume r WHERE r.user.email = :userEmail ORDER BY r.lastUpdated DESC")
    List<Resume> findLatestResumesByUserEmail(@Param("userEmail") String userEmail);
    
    @Query("SELECT r FROM Resume r WHERE r.user = :user ORDER BY r.uploadedAt DESC")
    List<Resume> findTopByUserOrderByUploadedAtDesc(@Param("user") com.vivek.spring_boot_rest.model.User user);
} 