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

        User user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User 404"));

        return new UserPrincipal(user);
    }
}

