package com.vivek.spring_boot_rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.ListCrudRepository;

import com.vivek.spring_boot_rest.model.JobPost;

import java.util.List;


@Repository
public interface JobRepo extends JpaRepository<JobPost, Integer> {
    List<JobPost> findByPostProfileContainingOrPostDescContaining(String postProfile, String postDesc);
}
