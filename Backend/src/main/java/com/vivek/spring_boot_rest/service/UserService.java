package com.vivek.spring_boot_rest.service;

import com.vivek.spring_boot_rest.model.User;
import com.vivek.spring_boot_rest.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User saveUser(User user){
        System.out.println("Saving user: " + user.getUsername() + " with role: " + user.getRole());
        String rawPassword = user.getPassword();
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Password encoded for user: " + user.getUsername());
        user.setPassword(encodedPassword);
        // role is already set in controller
        User savedUser = repo.save(user);
        System.out.println("User saved successfully: " + savedUser.getUsername());
        return savedUser;
    }

    public User findByUsername(String username) {
        return repo.findByUsername(username).orElse(null);
    }
    
    public User findByUsernameAndPassword(String username, String rawPassword) {
        List<User> users = repo.findAllByUsername(username);
        if (users.isEmpty()) {
            return null;
        }
        
        // Try to match password with any of the users with this username
        for (User user : users) {
            if (encoder.matches(rawPassword, user.getPassword())) {
                return user;
            }
        }
        
        return null;
    }
    
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
