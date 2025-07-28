package com.vivek.spring_boot_rest.controller;

import com.vivek.spring_boot_rest.model.User;
import com.vivek.spring_boot_rest.dto.UserDto;
import com.vivek.spring_boot_rest.dto.LoginDto;
import com.vivek.spring_boot_rest.service.JwtService;
import com.vivek.spring_boot_rest.service.UserService;
import com.vivek.spring_boot_rest.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = {"http://localhost", "http://localhost:80", "http://frontend:80", "https://job-portal-ai-frontend.onrender.com"}, allowCredentials = "true")
@RequestMapping("/api/users")
@RestController
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public User register(@RequestBody UserDto userDto) {
        try {
            // Validate role
            String role = userDto.getRole() != null ? userDto.getRole().toUpperCase() : "USER";
            if (!role.equals("USER") && !role.equals("RECRUITER")) {
                throw new IllegalArgumentException("Invalid role. Only USER and RECRUITER roles are allowed for registration.");
            }
            
            // Create User entity from DTO
            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());
            user.setEmail(userDto.getEmail());
            user.setRole(role);
            User savedUser = service.saveUser(user);
            
            // Send welcome email (works with Gmail App Password or any SMTP)
            // For Gmail, use an App Password instead of your real password
            String subject = "Welcome to Job Portal!";
            String body = "Hello " + userDto.getUsername() + ",\n\nThank you for registering at Job Portal as a " + role + ". We're excited to have you on board!\n\nBest regards,\nJob Portal Team";
            try {
                emailService.sendEmail(userDto.getEmail(), subject, body);
            } catch (Exception e) {
                // Log email error but don't fail registration
                System.err.println("Failed to send welcome email: " + e.getMessage());
            }
            
            return savedUser;
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            throw e;
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        try {
            System.out.println("Login attempt for user: " + loginDto.getUsername());
            
            // First, let's check if the user exists
            User dbUser = service.findByUsername(loginDto.getUsername());
            if (dbUser == null) {
                System.out.println("User not found: " + loginDto.getUsername());
                return "Login Failed";
            }
            
            System.out.println("User found: " + loginDto.getUsername() + " with role: " + dbUser.getRole());
            
            // Test password matching manually
            System.out.println("Raw password from request: " + loginDto.getPassword());
            System.out.println("Encoded password from DB: " + dbUser.getPassword());
            boolean passwordMatches = service.checkPassword(loginDto.getPassword(), dbUser.getPassword());
            System.out.println("Password matches: " + passwordMatches);
            
            // Use manual password check instead of AuthenticationManager
            if (passwordMatches) {
                String token = jwtService.generateToken(loginDto.getUsername(), dbUser.getRole());
                System.out.println("Login successful for user: " + loginDto.getUsername());
                return token;
            } else {
                System.out.println("Login failed for user: " + loginDto.getUsername() + " - password does not match");
                return "Login Failed";
            }
        } catch (Exception e) {
            System.err.println("Login error for user " + loginDto.getUsername() + ": " + e.getMessage());
            e.printStackTrace();
            return "Login Failed";
        }
    }
    
    // Test endpoint to verify password (remove this in production)
    @PostMapping("/test-password")
    public String testPassword(@RequestBody LoginDto loginDto) {
        try {
            User dbUser = service.findByUsername(loginDto.getUsername());
            if (dbUser == null) {
                return "User not found";
            }
            
            boolean passwordMatches = service.checkPassword(loginDto.getPassword(), dbUser.getPassword());
            return "Password matches: " + passwordMatches + 
                   "\nRaw password: " + loginDto.getPassword() + 
                   "\nEncoded password: " + dbUser.getPassword();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

