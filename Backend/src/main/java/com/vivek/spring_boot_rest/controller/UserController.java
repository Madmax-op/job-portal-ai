package com.vivek.spring_boot_rest.controller;

import com.vivek.spring_boot_rest.model.User;
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

// DTO for user registration (add this as an inner class or import if exists)
class UserDto {
    public String username;
    public String password;
    public String email;
    public String role = "USER";
}

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
            String role = userDto.role != null ? userDto.role.toUpperCase() : "USER";
            if (!role.equals("USER") && !role.equals("RECRUITER")) {
                throw new IllegalArgumentException("Invalid role. Only USER and RECRUITER roles are allowed for registration.");
            }
            
            // Create User entity from DTO
            User user = new User();
            user.setUsername(userDto.username);
            user.setPassword(userDto.password);
            user.setEmail(userDto.email);
            user.setRole(role);
            User savedUser = service.saveUser(user);
            
            // Send welcome email (works with Gmail App Password or any SMTP)
            // For Gmail, use an App Password instead of your real password
            String subject = "Welcome to Job Portal!";
            String body = "Hello " + userDto.username + ",\n\nThank you for registering at Job Portal as a " + role + ". We're excited to have you on board!\n\nBest regards,\nJob Portal Team";
            try {
                emailService.sendEmail(userDto.email, subject, body);
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
    public String login(@RequestBody User user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            User dbUser = service.findByUsername(user.getUsername());
            return jwtService.generateToken(user.getUsername(), dbUser.getRole());
        } else {
            return "Login Failed";
        }
    }
}

