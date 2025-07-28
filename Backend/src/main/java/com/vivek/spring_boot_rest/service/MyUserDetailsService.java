package com.vivek.spring_boot_rest.service;

import com.vivek.spring_boot_rest.model.User;
import com.vivek.spring_boot_rest.model.UserPrincipal;
import com.vivek.spring_boot_rest.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Looking for user: " + username);

        Optional<User> userOpt = repo.findByUsername(username);
        if (userOpt.isEmpty()) {
            System.out.println("User not found: " + username);
            throw new UsernameNotFoundException("User 404: " + username);
        }

        User user = userOpt.get();
        System.out.println("User found: " + username + " with role: " + user.getRole());
        return new UserPrincipal(user);
    }
}

