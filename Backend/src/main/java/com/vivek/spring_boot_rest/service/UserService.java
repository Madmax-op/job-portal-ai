package com.vivek.spring_boot_rest.service;

import com.vivek.spring_boot_rest.model.User;
import com.vivek.spring_boot_rest.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User saveUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        // role is already set in controller
        return repo.save(user);
    }

    public User findByUsername(String username) {
        return repo.findByUsername(username).orElse(null);
    }
}
