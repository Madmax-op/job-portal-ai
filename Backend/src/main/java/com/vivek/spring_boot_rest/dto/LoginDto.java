package com.vivek.spring_boot_rest.dto;

public class LoginDto {
    private String username;
    private String password;
    
    public LoginDto() {}
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
} 