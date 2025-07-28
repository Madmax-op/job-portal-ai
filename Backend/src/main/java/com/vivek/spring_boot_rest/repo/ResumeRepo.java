package com.vivek.spring_boot_rest.repo;

import com.vivek.spring_boot_rest.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepo extends JpaRepository<Resume, Long> {
    
    List<Resume> findByUserId(Integer userId);
    
    @Query("SELECT r FROM Resume r WHERE r.user.id = :userId ORDER BY r.lastUpdated DESC")
    List<Resume> findLatestResumesByUserId(@Param("userId") Integer userId);
    
    @Query("SELECT r FROM Resume r WHERE r.user = :user ORDER BY r.uploadedAt DESC")
    List<Resume> findTopByUserOrderByUploadedAtDesc(@Param("user") com.vivek.spring_boot_rest.model.User user);
} 