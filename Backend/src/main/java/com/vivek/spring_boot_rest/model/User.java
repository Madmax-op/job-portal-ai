package com.vivek.spring_boot_rest.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="users")
@Data
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private String email;
    private String username;
    private String password;
    @Column(nullable = false)
    private String role = "USER";
    
    // Manual getters and setters for compatibility
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
