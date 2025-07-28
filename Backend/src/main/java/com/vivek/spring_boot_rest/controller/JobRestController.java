package com.vivek.spring_boot_rest.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.vivek.spring_boot_rest.model.JobPost;
import com.vivek.spring_boot_rest.service.JobService;

@CrossOrigin(origins = {"http://localhost", "http://localhost:80", "http://frontend:80", "https://job-portal-ai-frontend.onrender.com"}, allowCredentials = "true")
@RequestMapping("/api/jobs")
@RestController
public class JobRestController {

    @Autowired
    private JobService service;
    @GetMapping("/all")
    public List<JobPost> getAllJobs() {
        return service.getAllJobs();
    }
    @GetMapping("/post/{postId}")
    public JobPost getJob(@PathVariable int postId) {
        return service.getJob(postId);
    }
    @PostMapping("/post")
    public JobPost addJob(@RequestBody JobPost jobPost) {
        service.addJob(jobPost);
        return service.getJob(jobPost.getPostId());
    }
    @PutMapping("/post")
    public JobPost updateJob(@RequestBody JobPost jobPost) {
        service.updateJob(jobPost);
        return service.getJob(jobPost.getPostId());
    }
    @DeleteMapping("/post/{postId}")
    public String deleteJob(@PathVariable int postId) {
        service.deleteJob(postId);
        return "Deleted";
    }
    @GetMapping("/load")
    public String loadData() {
        service.load();
        return "success";
    }
}