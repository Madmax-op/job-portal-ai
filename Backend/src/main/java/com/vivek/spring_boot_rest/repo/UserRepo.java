package com.vivek.spring_boot_rest.repo;

import com.vivek.spring_boot_rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
        Optional<User> findByUsername(String username);
}
