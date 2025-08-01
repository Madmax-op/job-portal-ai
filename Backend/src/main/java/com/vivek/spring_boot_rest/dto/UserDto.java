package com.vivek.spring_boot_rest.dto;

public class UserDto {
    private String username;
    private String password;
    private String email;
    private String role = "USER";
    
    public UserDto() {}
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
} 